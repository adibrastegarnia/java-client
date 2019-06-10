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

import java.util.concurrent.CompletableFuture;

import io.atomix.api.headers.Name;
import io.atomix.client.PrimitiveManagementService;
import io.atomix.client.log.DistributedLog;
import io.atomix.client.log.DistributedLogBuilder;

/**
 * Default distributed log builder.
 */
public class DefaultDistributedLogBuilder<E> extends DistributedLogBuilder<E> {
    public DefaultDistributedLogBuilder(Name name, PrimitiveManagementService managementService) {
        super(name, managementService);
    }

    @Override
    public CompletableFuture<DistributedLog<E>> buildAsync() {
        return managementService.getPartitionService().getPartitionGroup(group)
            .thenApply(group -> new DefaultAsyncDistributedLog<E>(
                getName(),
                group,
                partitioner,
                serializer)
                .sync());
    }
}
