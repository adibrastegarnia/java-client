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

import io.atomix.api.primitive.Name;
import io.atomix.client.PrimitiveManagementService;
import io.atomix.client.PrimitiveType;
import io.atomix.client.counter.impl.DefaultDistributedCounterBuilder;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Distributed counter primitive type.
 */
public class DistributedCounterType implements PrimitiveType<DistributedCounterBuilder, DistributedCounter> {
    private static final DistributedCounterType INSTANCE = new DistributedCounterType();

    /**
     * Returns a new distributed counter type.
     *
     * @return a new distributed counter type
     */
    public static DistributedCounterType instance() {
        return INSTANCE;
    }

    @Override
    public DistributedCounterBuilder newBuilder(Name name, PrimitiveManagementService managementService) {
        return new DefaultDistributedCounterBuilder(name, managementService);
    }

    @Override
    public String toString() {
        return toStringHelper(this).toString();
    }
}
