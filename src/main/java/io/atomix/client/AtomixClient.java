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
package io.atomix.client;

import io.atomix.api.controller.*;
import io.atomix.client.channel.ChannelConfig;
import io.atomix.client.channel.ChannelProvider;
import io.atomix.client.channel.ServiceChannelProvider;
import io.atomix.client.impl.DefaultPrimitiveManagementService;
import io.atomix.client.impl.PrimitiveCacheImpl;
import io.atomix.client.partition.impl.PartitionServiceImpl;
import io.atomix.client.utils.concurrent.BlockingAwareThreadPoolContextFactory;
import io.atomix.client.utils.concurrent.ThreadContextFactory;
import io.grpc.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Primary interface for managing Atomix clusters and operating on distributed primitives.
 * <p>
 * The {@code Atomix} class is the primary interface to all Atomix features. To construct an {@code Atomix} instance,
 * either configure the instance with a configuration file or construct a new instance from an {@link AtomixClientBuilder}.
 * Builders can be created via various {@link #builder()} methods:
 * <pre>
 *   {@code
 *   Atomix atomix = Atomix.builder()
 *     .withMemberId("member-1")
 *     .withHost("192.168.10.2")
 *     .build();
 *   }
 * </pre>
 * Once an {@code Atomix} instance has been constructed, start the instance by calling {@link #start()}:
 * <pre>
 *   {@code
 *   atomix.start().join();
 *   }
 * </pre>
 * The returned {@link CompletableFuture} will be completed once the node has been bootstrapped and all services are
 * available.
 * <p>
 * The instance can be used to access services for managing the cluster or communicating with other nodes. Additionally,
 * it provides various methods for creating and operating on distributed primitives. Generally, the primitive methods
 * are separated into two types. Primitive getters return multiton instances of a primitive. Primitives created via
 * getters must be pre-configured in the Atomix instance configuration. Alternatively, primitive builders can be used to
 * create and configure primitives in code:
 * <pre>
 *   {@code
 *   DistributedMap<String, String> map = atomix.mapBuilder("my-map")
 *     .withProtocol(MultiRaftProtocol.builder("raft")
 *       .withReadConsistency(ReadConsistency.SEQUENTIAL)
 *       .build())
 *     .build();
 *   }
 * </pre>
 * Custom primitives can be constructed by providing a custom {@link PrimitiveType} and using the {@link
 * #primitiveBuilder(String, PrimitiveType)} method:
 * <pre>
 *   {@code
 *   MyPrimitive myPrimitive = atomix.primitiveBuilder("my-primitive, MyPrimitiveType.instance())
 *     .withProtocol(MultiRaftProtocol.builder("raft")
 *       .withReadConsistency(ReadConsistency.SEQUENTIAL)
 *       .build())
 *     .build();
 *   }
 * </pre>
 */
public class AtomixClient {

    /**
     * Returns a new Atomix client builder.
     *
     * @return a new Atomix builder
     */
    public static AtomixClientBuilder builder() {
        return new AtomixClientBuilder();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AtomixClient.class);

    private final String namespace;
    private final ChannelProvider channelProvider;
    private final PrimitiveCache primitiveCache = new PrimitiveCacheImpl();
    private ThreadContextFactory threadContextFactory;
    private PrimitiveManagementService managementService;
    private final AtomicBoolean started = new AtomicBoolean();

    protected AtomixClient(String namespace, ChannelProvider channelProvider) {
        this.namespace = namespace;
        this.channelProvider = channelProvider;
    }

    /**
     * Returns a list of databases in the cluster.
     *
     * @return a list of databases supported by the controller
     */
    public Collection<AtomixDatabase> getDatabases() {
        Channel channel = channelProvider.getFactory().getChannel();
        ControllerServiceGrpc.ControllerServiceBlockingStub controller = ControllerServiceGrpc.newBlockingStub(channel);
        GetDatabasesResponse response = controller.getDatabases(GetDatabasesRequest.newBuilder()
            .setId(DatabaseId.newBuilder()
                .setNamespace(namespace)
                .build())
            .build());

        List<AtomixDatabase> databases = new ArrayList<>();
        for (Database database : response.getDatabasesList()) {
            databases.add(new AtomixDatabase(
                database.getId().getName(),
                database.getId().getNamespace(),
                new ServiceChannelProvider(database.getId().getName(), new ChannelConfig())));
        }
        return databases;
    }

    /**
     * Returns a database by name.
     *
     * @param name the database name
     * @return the database
     */
    public AtomixDatabase getDatabase(String name) {
        Channel channel = channelProvider.getFactory().getChannel();
        ControllerServiceGrpc.ControllerServiceBlockingStub controller = ControllerServiceGrpc.newBlockingStub(channel);
        GetDatabasesResponse response = controller.getDatabases(GetDatabasesRequest.newBuilder()
            .setId(DatabaseId.newBuilder()
                .setNamespace(namespace)
                .setName(name)
                .build())
            .build());
        if (response.getDatabasesCount() == 0) {
            return null;
        }
        Database database = response.getDatabases(0);
        return new AtomixDatabase(
            database.getId().getName(),
            database.getId().getNamespace(),
            new ServiceChannelProvider(database.getId().getName(), new ChannelConfig()));
    }

    /**
     * Starts the Atomix instance.
     * <p>
     * The returned future will be completed once this instance completes startup. Note that in order to complete startup,
     * all partitions must be able to form. For Raft partitions, that requires that a majority of the nodes in each
     * partition be started concurrently.
     *
     * @return a future to be completed once the instance has completed startup
     */
    public synchronized CompletableFuture<AtomixClient> start() {
        this.threadContextFactory = new BlockingAwareThreadPoolContextFactory(
            "atomix-client-%d",
            Runtime.getRuntime().availableProcessors(),
            LOGGER);
        this.managementService = new DefaultPrimitiveManagementService(
            new PartitionServiceImpl(channelProvider.getFactory()),
            primitiveCache,
            threadContextFactory);
        started.set(true);
        LOGGER.info("Started");
        return CompletableFuture.completedFuture(this);
    }

    /**
     * Returns a boolean indicating whether the instance is running.
     *
     * @return indicates whether the instance is running
     */
    public boolean isRunning() {
        return started.get();
    }

    /**
     * Stops the instance.
     *
     * @return a future to be completed once the instance has been stopped
     */
    public synchronized CompletableFuture<Void> stop() {
        threadContextFactory.close();
        LOGGER.info("Stopped");
        started.set(false);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public String toString() {
        return toStringHelper(this).toString();
    }
}
