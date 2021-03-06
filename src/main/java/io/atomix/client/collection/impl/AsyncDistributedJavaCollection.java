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
package io.atomix.client.collection.impl;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import io.atomix.client.PrimitiveType;
import io.atomix.client.collection.AsyncDistributedCollection;
import io.atomix.client.collection.CollectionEventListener;
import io.atomix.client.collection.DistributedCollection;
import io.atomix.client.collection.DistributedCollectionType;
import io.atomix.client.iterator.AsyncIterator;
import io.atomix.client.iterator.impl.AsyncJavaIterator;

/**
 * Asynchronous distributed Java-backed collection.
 */
public class AsyncDistributedJavaCollection<E> implements AsyncDistributedCollection<E> {
    private final String name;
    private final Collection<E> collection;

    public AsyncDistributedJavaCollection(String name, Collection<E> collection) {
        this.name = name;
        this.collection = collection;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public PrimitiveType type() {
        return DistributedCollectionType.instance();
    }

    @Override
    public CompletableFuture<Boolean> add(E element) {
        return CompletableFuture.completedFuture(collection.add(element));
    }

    @Override
    public CompletableFuture<Boolean> remove(E element) {
        return CompletableFuture.completedFuture(collection.remove(element));
    }

    @Override
    public CompletableFuture<Integer> size() {
        return CompletableFuture.completedFuture(collection.size());
    }

    @Override
    public CompletableFuture<Boolean> isEmpty() {
        return CompletableFuture.completedFuture(collection.isEmpty());
    }

    @Override
    public CompletableFuture<Void> clear() {
        collection.clear();
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Boolean> contains(E element) {
        return CompletableFuture.completedFuture(collection.contains(element));
    }

    @Override
    public CompletableFuture<Boolean> addAll(Collection<? extends E> c) {
        return CompletableFuture.completedFuture(collection.addAll(c));
    }

    @Override
    public CompletableFuture<Boolean> containsAll(Collection<? extends E> c) {
        return CompletableFuture.completedFuture(collection.containsAll(c));
    }

    @Override
    public CompletableFuture<Boolean> retainAll(Collection<? extends E> c) {
        return CompletableFuture.completedFuture(collection.retainAll(c));
    }

    @Override
    public CompletableFuture<Boolean> removeAll(Collection<? extends E> c) {
        return CompletableFuture.completedFuture(collection.removeAll(c));
    }

    @Override
    public CompletableFuture<Void> addListener(CollectionEventListener<E> listener, Executor executor) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> removeListener(CollectionEventListener<E> listener) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public AsyncIterator<E> iterator() {
        return new AsyncJavaIterator<>(collection.iterator());
    }

    @Override
    public CompletableFuture<Void> close() {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> delete() {
        collection.clear();
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public DistributedCollection<E> sync(Duration operationTimeout) {
        return new BlockingDistributedCollection<>(this, operationTimeout.toMillis());
    }
}
