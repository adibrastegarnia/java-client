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
package io.atomix.client.iterator;

import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

import io.atomix.client.DistributedPrimitive;
import io.atomix.client.iterator.impl.BlockingIterator;

/**
 * Asynchronous iterator.
 */
public interface AsyncIterator<T> {

    /**
     * Returns whether the iterator has a next item.
     *
     * @return whether a next item exists in the iterator
     */
    CompletableFuture<Boolean> hasNext();

    /**
     * Returns the next item in the iterator.
     *
     * @return the next item in the iterator
     */
    CompletableFuture<T> next();

    /**
     * Returns a synchronous iterator.
     *
     * @return the synchronous iterator
     */
    default Iterator<T> sync() {
        return sync(Duration.ofMillis(DistributedPrimitive.DEFAULT_OPERATION_TIMEOUT_MILLIS));
    }

    /**
     * Returns a synchronous iterator.
     *
     * @param timeout the iterator operation timeout
     * @return the synchronous iterator
     */
    default Iterator<T> sync(Duration timeout) {
        return new BlockingIterator<>(this, timeout.toMillis());
    }
}
