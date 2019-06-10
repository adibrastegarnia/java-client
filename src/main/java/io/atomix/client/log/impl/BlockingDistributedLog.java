/*
 * Copyright 2019-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.client.log.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import com.google.common.base.Throwables;
import io.atomix.client.PrimitiveException;
import io.atomix.client.Synchronous;
import io.atomix.client.log.AsyncDistributedLog;
import io.atomix.client.log.DistributedLog;
import io.atomix.client.log.DistributedLogPartition;
import io.atomix.client.log.Record;

/**
 * Default distributed log.
 */
public class BlockingDistributedLog<E> extends Synchronous<AsyncDistributedLog<E>> implements DistributedLog<E> {
    private final AsyncDistributedLog<E> asyncLog;
    private final Map<Integer, DistributedLogPartition<E>> partitions = new ConcurrentHashMap<>();
    private final List<DistributedLogPartition<E>> sortedPartitions = new CopyOnWriteArrayList<>();
    private final long operationTimeoutMillis;

    public BlockingDistributedLog(AsyncDistributedLog<E> asyncLog, long operationTimeoutMillis) {
        super(asyncLog);
        this.asyncLog = asyncLog;
        this.operationTimeoutMillis = operationTimeoutMillis;
        asyncLog.getPartitions().forEach(partition -> {
            DistributedLogPartition<E> blockingPartition = new BlockingDistributedLogPartition<>(partition, operationTimeoutMillis);
            partitions.put(blockingPartition.id(), blockingPartition);
            sortedPartitions.add(blockingPartition);
        });
    }

    @Override
    public List<DistributedLogPartition<E>> getPartitions() {
        return sortedPartitions;
    }

    @Override
    public DistributedLogPartition<E> getPartition(int partitionId) {
        return partitions.get(partitionId);
    }

    @Override
    public DistributedLogPartition<E> getPartition(E entry) {
        return getPartition(asyncLog.getPartition(entry).id());
    }

    @Override
    public void produce(E entry) {
        complete(asyncLog.produce(entry));
    }

    @Override
    public void consume(Consumer<Record<E>> consumer) {
        complete(asyncLog.consume(consumer));
    }

    @Override
    public AsyncDistributedLog<E> async() {
        return asyncLog;
    }

    private <T> T complete(CompletableFuture<T> future) {
        try {
            return future.get(operationTimeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PrimitiveException.Interrupted();
        } catch (TimeoutException e) {
            throw new PrimitiveException.ConcurrentModification();
        } catch (ExecutionException e) {
            Throwable cause = Throwables.getRootCause(e);
            if (cause instanceof PrimitiveException) {
                throw (PrimitiveException) cause;
            } else {
                throw new PrimitiveException(cause);
            }
        }
    }
}
