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

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import io.atomix.client.collection.AsyncDistributedCollection;
import io.atomix.client.utils.concurrent.Futures;

/**
 * Unmodifiable distributed collection.
 */
public class UnmodifiableAsyncDistributedCollection<E> extends DelegatingAsyncDistributedCollection<E> {
    private static final String ERROR_MSG = "updates are not allowed";

    public UnmodifiableAsyncDistributedCollection(AsyncDistributedCollection<E> delegateCollection) {
        super(delegateCollection);
    }

    @Override
    public CompletableFuture<Boolean> add(E element) {
        return Futures.exceptionalFuture(new UnsupportedOperationException(ERROR_MSG));
    }

    @Override
    public CompletableFuture<Boolean> remove(E element) {
        return Futures.exceptionalFuture(new UnsupportedOperationException(ERROR_MSG));
    }

    @Override
    public CompletableFuture<Boolean> addAll(Collection<? extends E> c) {
        return Futures.exceptionalFuture(new UnsupportedOperationException(ERROR_MSG));
    }

    @Override
    public CompletableFuture<Boolean> containsAll(Collection<? extends E> c) {
        return Futures.exceptionalFuture(new UnsupportedOperationException(ERROR_MSG));
    }

    @Override
    public CompletableFuture<Boolean> retainAll(Collection<? extends E> c) {
        return Futures.exceptionalFuture(new UnsupportedOperationException(ERROR_MSG));
    }

    @Override
    public CompletableFuture<Boolean> removeAll(Collection<? extends E> c) {
        return Futures.exceptionalFuture(new UnsupportedOperationException(ERROR_MSG));
    }

    @Override
    public CompletableFuture<Void> clear() {
        return Futures.exceptionalFuture(new UnsupportedOperationException(ERROR_MSG));
    }
}
