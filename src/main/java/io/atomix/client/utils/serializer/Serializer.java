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

package io.atomix.client.utils.serializer;

/**
 * Interface for serialization of store artifacts.
 */
public interface Serializer {

    /**
     * Serialize the specified object.
     *
     * @param object object to serialize.
     * @param <T>    encoded type
     * @return serialized bytes.
     */
    <T> byte[] encode(T object);

    /**
     * Deserialize the specified bytes.
     *
     * @param bytes byte array to deserialize.
     * @param <T>   decoded type
     * @return deserialized object.
     */
    <T> T decode(byte[] bytes);

}