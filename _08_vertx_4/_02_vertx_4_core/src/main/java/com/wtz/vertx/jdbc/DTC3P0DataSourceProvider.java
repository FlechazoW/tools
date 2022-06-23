/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wtz.vertx.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.spi.impl.C3P0DataSourceProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.SQLException;

import static com.wtz.vertx.jdbc.C3P0Constants.ACQUIRE_INCREMENT_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.ACQUIRE_RETRY_ATTEMPTS_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.ACQUIRE_RETRY_DELAY_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.AUTOMATIC_TEST_TABLE_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.AUTO_COMMIT_ON_CLOSE_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.BREAK_AFTER_ACQUIRE_FAILURE_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.CHECKOUT_TIMEOUT_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.CONNECTION_CUSTOMIZER_CLASS_NAME;
import static com.wtz.vertx.jdbc.C3P0Constants.CONNECTION_TESTER_CLASS_NAME;
import static com.wtz.vertx.jdbc.C3P0Constants.DEBUG_UNRETURNED_CONNECTION_STACK_TRACES_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.DRIVER_NAME_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.FORCE_IGNORE_UNRESOLVED_TRANSACTIONS_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.FORCE_SYNCHRONOUS_CHECKINS_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.IDLE_CONNECTION_TEST_PERIOD_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.INITIAL_POOL_SIZE_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.MAX_CONNECTION_AGE_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.MAX_IDLE_TIME_EXCESS_CONNECTIONS_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.MAX_IDLE_TIME_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.MAX_POOL_SIZE_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.MAX_STATEMENTS_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.MAX_STATEMENTS_PRE_CONNECTION;
import static com.wtz.vertx.jdbc.C3P0Constants.MIN_POOL_SIZE_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.PASSWORD_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.PREFERRED_TEST_QUERY_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.PROPERTY_CYCLE_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.TEST_CONNECTION_ON_CHECKIN_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.TEST_CONNECTION_ON_CHECKOUT_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.UNRETURNED_CONNECTION_TIMEOUT_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.USER_KEY;

/**
 * Date: 2019/9/17
 * Company: www.dtstack.com
 *
 * @author maqi
 */
public class DTC3P0DataSourceProvider extends C3P0DataSourceProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DTC3P0DataSourceProvider.class);

    @Override
    public DataSource getDataSource(JsonObject config) throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Get c3p0 config: \n" + config.encodePrettily());
        }

        String url = config.getString("url");
        if (url == null) {
            throw new NullPointerException("url cannot be null");
        }
        String driverClass = config.getString(DRIVER_NAME_KEY);
        String user = config.getString(USER_KEY);
        String password = config.getString(PASSWORD_KEY);
        //add c3p0 params

        Integer acquireIncrement = config.getInteger(ACQUIRE_INCREMENT_KEY);
        Integer acquireRetryAttempts = config.getInteger(ACQUIRE_RETRY_ATTEMPTS_KEY);
        Integer acquireRetryDelay = config.getInteger(ACQUIRE_RETRY_DELAY_KEY);
        Boolean autoCommitOnClose = config.getBoolean(AUTO_COMMIT_ON_CLOSE_KEY);
        String automaticTestTable = config.getString(AUTOMATIC_TEST_TABLE_KEY);
        Boolean breakAfterAcquireFailure = config.getBoolean(BREAK_AFTER_ACQUIRE_FAILURE_KEY);
        Integer checkoutTimeout = config.getInteger(CHECKOUT_TIMEOUT_KEY);
        String connectionCustomizerClassname = config.getString(CONNECTION_CUSTOMIZER_CLASS_NAME);
        String connectionTesterClassname = config.getString(CONNECTION_TESTER_CLASS_NAME);
        Boolean debugUnreturnedConnectionStackTraces = config.getBoolean(DEBUG_UNRETURNED_CONNECTION_STACK_TRACES_KEY);
        Boolean forceIgnoreUnresolvedTransactions = config.getBoolean(FORCE_IGNORE_UNRESOLVED_TRANSACTIONS_KEY);
        Boolean forceSynchronousCheckins = config.getBoolean(FORCE_SYNCHRONOUS_CHECKINS_KEY);
        Integer idleConnectionTestPeriod = config.getInteger(IDLE_CONNECTION_TEST_PERIOD_KEY);
        Integer initialPoolSize = config.getInteger(INITIAL_POOL_SIZE_KEY);
        Integer maxConnectionAge = config.getInteger(MAX_CONNECTION_AGE_KEY);
        Integer maxIdleTime = config.getInteger(MAX_IDLE_TIME_KEY);
        Integer maxIdleTimeExcessConnections = config.getInteger(MAX_IDLE_TIME_EXCESS_CONNECTIONS_KEY);
        Integer maxPoolSize = config.getInteger(MAX_POOL_SIZE_KEY);
        Integer maxStatements = config.getInteger(MAX_STATEMENTS_KEY);
        Integer maxStatementsPerConnection = config.getInteger(MAX_STATEMENTS_PRE_CONNECTION);
        Integer minPoolSize = config.getInteger(MIN_POOL_SIZE_KEY);
        String preferredTestQuery = config.getString(PREFERRED_TEST_QUERY_KEY);
        Integer propertyCycle = config.getInteger(PROPERTY_CYCLE_KEY);
        Boolean testConnectionOnCheckin = config.getBoolean(TEST_CONNECTION_ON_CHECKIN_KEY);
        Boolean testConnectionOnCheckout = config.getBoolean(TEST_CONNECTION_ON_CHECKOUT_KEY);
        Integer unreturnedConnectionTimeout = config.getInteger(UNRETURNED_CONNECTION_TIMEOUT_KEY);

        // If you want to configure any other C3P0 properties you can add a file c3p0.properties to the classpath
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setJdbcUrl(url);
        if (driverClass != null) {
            try {
                cpds.setDriverClass(driverClass);
            } catch (PropertyVetoException e) {
                throw new IllegalArgumentException(e);
            }
        }
        if (user != null) {
            cpds.setUser(user);
        }
        if (password != null) {
            cpds.setPassword(password);
        }
        if (maxPoolSize != null) {
            cpds.setMaxPoolSize(maxPoolSize);
        }
        if (minPoolSize != null) {
            cpds.setMinPoolSize(minPoolSize);
        }
        if (initialPoolSize != null) {
            cpds.setInitialPoolSize(initialPoolSize);
        }
        if (maxStatements != null) {
            cpds.setMaxStatements(maxStatements);
        }
        if (maxStatementsPerConnection != null) {
            cpds.setMaxStatementsPerConnection(maxStatementsPerConnection);
        }
        if (maxIdleTime != null) {
            cpds.setMaxIdleTime(maxIdleTime);
        }
        if (acquireRetryAttempts != null) {
            cpds.setAcquireRetryAttempts(acquireRetryAttempts);
        }
        if (acquireRetryDelay != null) {
            cpds.setAcquireRetryDelay(acquireRetryDelay);
        }
        if (breakAfterAcquireFailure != null) {
            cpds.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
        }

        if (StringUtils.isNotEmpty(preferredTestQuery)) {
            cpds.setPreferredTestQuery(preferredTestQuery);
        }

        if (idleConnectionTestPeriod != null) {
            cpds.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
        }

        if (testConnectionOnCheckin != null) {
            cpds.setTestConnectionOnCheckin(testConnectionOnCheckin);
        }

        if (acquireIncrement != null) {
            cpds.setAcquireIncrement(acquireIncrement);
        }

        if (autoCommitOnClose != null) {
            cpds.setAutoCommitOnClose(autoCommitOnClose);
        }

        if (automaticTestTable != null) {
            cpds.setAutomaticTestTable(automaticTestTable);
        }

        if (checkoutTimeout != null) {
            cpds.setCheckoutTimeout(checkoutTimeout);
        }

        if (connectionCustomizerClassname != null) {
            cpds.setConnectionCustomizerClassName(connectionCustomizerClassname);
        }

        if (connectionTesterClassname != null) {
            try {
                cpds.setConnectionTesterClassName(connectionTesterClassname);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (debugUnreturnedConnectionStackTraces != null) {
            cpds.setDebugUnreturnedConnectionStackTraces(debugUnreturnedConnectionStackTraces);
        }

        if (forceIgnoreUnresolvedTransactions != null) {
            cpds.setForceIgnoreUnresolvedTransactions(forceIgnoreUnresolvedTransactions);
        }

        if (forceSynchronousCheckins != null) {
            cpds.setForceSynchronousCheckins(forceSynchronousCheckins);
        }

        if (maxConnectionAge != null) {
            cpds.setMaxConnectionAge(maxConnectionAge);
        }

        if (maxIdleTimeExcessConnections != null) {
            cpds.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
        }

        if (propertyCycle != null) {
            cpds.setPropertyCycle(propertyCycle);
        }

        if (testConnectionOnCheckout != null) {
            cpds.setTestConnectionOnCheckout(testConnectionOnCheckout);
        }

        if (unreturnedConnectionTimeout != null) {
            cpds.setUnreturnedConnectionTimeout(unreturnedConnectionTimeout);
        }
        return cpds;
    }
}
