/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wtz.vertx.jdbc;

public class VertxJdbcClientConstant {

    private VertxJdbcClientConstant() throws IllegalAccessException {
        throw new IllegalAccessException(getClass() + " can not be instantiated.");
    }

    public static final String JDBC_URL_KEY = "jdbcUrl";

    public static final String USER_NAME_KEY = "user";

    public static final String PASSWORD_KEY = "password";

    public static final String DATABASE_KEY = "database";

    public static final String IDLE_TIMEOUT_KEY = "idleTimeout";

    public static final String METRICS_ENABLED_KEY = "metricsEnabled";

    public static final String TRACING_POLICY_KEY = "tracingPolicy";

    public static final String CONNECTION_TIMEOUT_KEY = "connectionTimeout";

    public static final String CONNECTION_TIMEOUT_UNIT_KEY = "connectionTimeoutUnit";

    public static final String EVENT_LOOP_SIZE_KEY = "eventLoopSize";

    public static final String MAX_SIZE_KEY = "maxSize";

    public static final String MAX_WAIT_QUEUE_SIZE_KEY = "maxWaitQueueSize";

    public static final String POOL_CLEANER_PERIOD_KEY = "poolCleanerPeriod";

    public static final String SHARED_KEY = "shared";
}
