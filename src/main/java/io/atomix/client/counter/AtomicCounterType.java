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
package io.atomix.client.counter;

import io.atomix.api.headers.Name;
import io.atomix.client.PrimitiveManagementService;
import io.atomix.client.PrimitiveType;
import io.atomix.client.counter.impl.DefaultAtomicCounterBuilder;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Atomic counter primitive type.
 */
public class AtomicCounterType implements PrimitiveType<AtomicCounterBuilder, AtomicCounter> {
    private static final String NAME = "atomic-counter";
    private static final AtomicCounterType INSTANCE = new AtomicCounterType();

    /**
     * Returns a new atomic counter type.
     *
     * @return a new atomic counter type
     */
    public static AtomicCounterType instance() {
        return INSTANCE;
    }

    @Override
    public AtomicCounterBuilder newBuilder(Name name, PrimitiveManagementService managementService) {
        return new DefaultAtomicCounterBuilder(name, managementService);
    }

    @Override
    public String toString() {
        return toStringHelper(this).toString();
    }
}
