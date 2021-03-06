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

/**
 * Distributed primitive exception.
 */
public class PrimitiveException extends AtomixClientException {
    public PrimitiveException() {
    }

    public PrimitiveException(String message) {
        super(message);
    }

    public PrimitiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrimitiveException(Throwable cause) {
        super(cause);
    }

    public static class ClosedSession extends PrimitiveException {
        public ClosedSession() {
        }

        public ClosedSession(String message) {
            super(message);
        }

        public ClosedSession(String message, Throwable cause) {
            super(message, cause);
        }

        public ClosedSession(Throwable cause) {
            super(cause);
        }
    }

    public static class UnknownClient extends PrimitiveException {
        public UnknownClient() {
        }

        public UnknownClient(String message) {
            super(message);
        }

        public UnknownClient(String message, Throwable cause) {
            super(message, cause);
        }

        public UnknownClient(Throwable cause) {
            super(cause);
        }
    }

    public static class UnknownSession extends PrimitiveException {
        public UnknownSession() {
        }

        public UnknownSession(String message) {
            super(message);
        }

        public UnknownSession(String message, Throwable cause) {
            super(message, cause);
        }

        public UnknownSession(Throwable cause) {
            super(cause);
        }
    }

    public static class UnknownService extends PrimitiveException {
        public UnknownService() {
        }

        public UnknownService(String message) {
            super(message);
        }

        public UnknownService(String message, Throwable cause) {
            super(message, cause);
        }

        public UnknownService(Throwable cause) {
            super(cause);
        }
    }

    public static class CommandFailure extends PrimitiveException {
        public CommandFailure() {
        }

        public CommandFailure(String message) {
            super(message);
        }

        public CommandFailure(String message, Throwable cause) {
            super(message, cause);
        }

        public CommandFailure(Throwable cause) {
            super(cause);
        }
    }

    public static class QueryFailure extends PrimitiveException {
        public QueryFailure() {
        }

        public QueryFailure(String message) {
            super(message);
        }

        public QueryFailure(String message, Throwable cause) {
            super(message, cause);
        }

        public QueryFailure(Throwable cause) {
            super(cause);
        }
    }

    public static class Interrupted extends PrimitiveException {
        public Interrupted() {
        }

        public Interrupted(String message) {
            super(message);
        }

        public Interrupted(String message, Throwable cause) {
            super(message, cause);
        }

        public Interrupted(Throwable cause) {
            super(cause);
        }
    }

    public static class Timeout extends PrimitiveException {
        public Timeout() {
        }

        public Timeout(String message) {
            super(message);
        }

        public Timeout(String message, Throwable cause) {
            super(message, cause);
        }

        public Timeout(Throwable cause) {
            super(cause);
        }
    }

    public static class ConcurrentModification extends PrimitiveException {
        public ConcurrentModification() {
        }

        public ConcurrentModification(String message) {
            super(message);
        }

        public ConcurrentModification(String message, Throwable cause) {
            super(message, cause);
        }

        public ConcurrentModification(Throwable cause) {
            super(cause);
        }
    }
}
