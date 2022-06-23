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

public class C3P0Constants {
    private C3P0Constants() throws IllegalAccessException {
        throw new IllegalAccessException(getClass() + " can not be instantiated.");
    }

    public static final String DRIVER_NAME_KEY = "driverName";

    public static final String USER_KEY = "userName";

    public static final String PASSWORD_KEY = "password";

    public static final String JDBC_URL_KEY = "jdbcUrl";

    public static final String SCHEMA_KEY = "schema";

    // c3p0 parameters

    // Determines how many connections at a time c3p0 will try to acquire when the pool is exhausted.
    public static final String ACQUIRE_INCREMENT_KEY = "acquireIncrement";
    // Defines how many times c3p0 will try to acquire a new Connection from the database before giving up.
    // If this value is less than or equal to zero, c3p0 will keep trying to fetch a Connection indefinitely.
    public static final String ACQUIRE_RETRY_ATTEMPTS_KEY = "acquireRetryAttempts";
    // Milliseconds, time c3p0 will wait between acquire attempts.
    public static final String ACQUIRE_RETRY_DELAY_KEY = "acquireRetryDelay";
    // The JDBC spec is unforgivably silent on what should happen to unresolved, pending transactions on Connection close.
    // C3P0's default policy is to rollback any uncommitted, pending work.
    // (I think this is absolutely, undeniably the right policy, but there is no consensus among JDBC driver vendors.)
    // Setting autoCommitOnClose to true causes uncommitted pending work to be committed, rather than rolled back on Connection close.
    // [Note: Since the spec is absurdly unclear on this question, application authors who wish to avoid bugs and inconsistent behavior should ensure that all transactions are explicitly either committed or rolled-back before close is called.]
    public static final String AUTO_COMMIT_ON_CLOSE_KEY = "autoCommitOnClose";
    // If provided, c3p0 will create an empty table of the specified name, and use queries against that table to test the Connection.
    // If automaticTestTable is provided, c3p0 will generate its own test query, therefore any preferredTestQuery set will be ignored.
    // You should not work with the named table after c3p0 creates it; it should be strictly for c3p0's use in testing your Connection.
    public static final String AUTOMATIC_TEST_TABLE_KEY = "automaticTestTable";
    // If true, a pooled DataSource will declare itself broken and be permanently closed if a Connection cannot be obtained from the database after making acquireRetryAttempts to acquire one.
    // If false, failure to obtain a Connection will cause all Threads waiting for the pool to acquire a Connection to throw an Exception, but the DataSource will remain valid, and will attempt to acquire again following a call to getConnection().
    public static final String BREAK_AFTER_ACQUIRE_FAILURE_KEY = "breakAfterAcquireFailure";
    // The number of milliseconds a client calling getConnection() will wait for a Connection to be checked-in or acquired when the pool is exhausted.
    // Zero means wait indefinitely. Setting any positive value will cause the getConnection() call to time-out and break with an SQLException after the specified number of milliseconds.
    public static final String CHECKOUT_TIMEOUT_KEY = "checkoutTimeout";
    // The fully qualified class-name of an implementation of the ConnectionCustomizer interface, which users can implement to set up Connections when they are acquired from the database, or on check-out, and potentially to clean things up on check-in and Connection destruction.
    // If standard Connection properties (holdability, readOnly, or transactionIsolation) are set in the ConnectionCustomizer's onAcquire() method, these will override the Connection default values.
    public static final String CONNECTION_CUSTOMIZER_CLASS_NAME = "connectionCustomizerClassName";
    // The fully qualified class-name of an implementation of the ConnectionTester interface, or QueryConnectionTester if you would like instances to have access to a user-configured preferredTestQuery.
    // This can be used to customize how c3p0 DataSources test Connections, but with the introduction of automaticTestTable and preferredTestQuery configuration parameters, "rolling your own" should be overkill for most users.
    public static final String CONNECTION_TESTER_CLASS_NAME = "connectionTesterClassName";
    // Must be one of caller, library, or none.
    // Determines how the contextClassLoader (see java.lang.Thread) of c3p0-spawned Threads is determined.
    // If caller, c3p0-spawned Threads (helper threads, java.util.Timer threads) inherit their contextClassLoader from the client Thread that provokes initialization of the pool.
    // If library, the contextClassLoader will be the class that loaded c3p0 classes.
    // If none, no contextClassLoader will be set (the property will be null), which in practice means the system ClassLoader will be used.
    // The default setting of caller is sometimes a problem when client applications will be hot redeployed by an app-server.
    // When c3p0's Threads hold a reference to a contextClassLoader from the first client that hits them, it may be impossible to garbage collect a ClassLoader associated with that client when it is undeploy in a running VM.
    // Setting this to library can resolve these issues.
    public static final String CONTEXT_CLASSLOADER_SOURCE_KEY = "contextClassLoaderSource";
    // If true, and if unreturnedConnectionTimeout is set to a positive value, then the pool will capture the stack trace (via an Exception) of all Connection checkouts, and the stack traces will be printed when unreturned checked-out Connections timeout.
    // This is intended to debug applications with Connection leaks, that is applications that occasionally fail to return Connections, leading to pool growth, and eventually exhaustion (when the pool hits maxPoolSize with all Connections checked-out and lost).
    // This parameter should only be set while debugging, as capturing the stack trace will slow down every Connection check-out.
    public static final String DEBUG_UNRETURNED_CONNECTION_STACK_TRACES_KEY = "debugUnreturnedConnectionStackTraces";
    // A java.util.Map (raw type) containing the values of any user-defined configuration extensions defined for this DataSource.
    public static final String EXTENSIONS_KEY = "extensions";
    // Strongly dis-recommended. Setting this to true may lead to subtle and bizarre bugs.
    // This is a terrible setting, leave it alone unless absolutely necessary.
    // It is here to workaround broken databases / JDBC drivers that do not properly support transactions, but that allow Connections' autoCommit flags to go to false regardless.
    // If you are using a database that supports transactions "partially" (this is oxymoronic, as the whole point of transactions is to perform operations reliably and completely, but nonetheless such databases are out there), if you feel comfortable ignoring the fact that Connections with autoCommit == false may be in the middle of transactions and may hold locks and other resources, you may turn off c3p0's wise default behavior, which is to protect itself, as well as the usability and consistency of the database, by either rolling back (default) or committing (see c3p0.autoCommitOnClose above) unresolved transactions.
    // This should only be set to true when you are sure you are using a database that allows Connections' autoCommit flag to go to false, but offers no other meaningful support of transactions. Otherwise, setting this to true is just a bad idea.
    public static final String FORCE_IGNORE_UNRESOLVED_TRANSACTIONS_KEY = "forceIgnoreUnresolvedTransactions";
    // Setting this to true forces Connections to be checked-in synchronously, which under some circumstances may improve performance.
    // Ordinarily Connections are checked-in asynchronously so that clients avoid any overhead of testing or custom check-in logic.
    // However, asynchronous check-in contributes to thread pool congestion, and very busy pools might find clients delayed waiting for check-ins to complete.
    // Expanding numHelperThreads can help manage Thread pool congestion, but memory footprint and switching costs put limits on practical thread pool size.
    // To reduce thread pool load, you can set forceSynchronousCheckins to true. Synchronous check-ins are likely to improve overall performance when testConnectionOnCheckin is set to false and no slow work is performed in a ConnectionCustomizer's onCheckIn(...) method.
    // If Connections are tested or other slow work is performed on check-in, then this setting will cause clients to experience the overhead of that work on Connection.close(), which you must trade-off against any improvements in pool performance.
    public static final String FORCE_SYNCHRONOUS_CHECKINS_KEY = "forceSynchronousCheckins";
    // Maximum number of Connections a pool will maintain at any given time.
    public static final String MAX_POOL_SIZE_KEY = "maxPoolSize";
    // Number of Connections a pool will try to acquire upon startup. Should be between minPoolSize and maxPoolSize.
    public static final String INITIAL_POOL_SIZE_KEY = "initialPoolSize";
    // Minimum number of Connections a pool will maintain at any given time.
    public static final String MIN_POOL_SIZE_KEY = "minPoolSize";
    // The size of c3p0's global PreparedStatement cache. If both maxStatements and maxStatementsPerConnection are zero, statement caching will not be enabled.
    // If maxStatements is zero but maxStatementsPerConnection is a non-zero value, statement caching will be enabled, but no global limit will be enforced, only the per-connection maximum.
    // maxStatements controls the total number of Statements cached, for all Connections.
    // If set, it should be a fairly large number, as each pooled Connection requires its own, distinct flock of cached statements.
    // As a guide, consider how many distinct PreparedStatements are used frequently in your application, and multiply that number by maxPoolSize to arrive at an appropriate value.
    // Though maxStatements is the JDBC standard parameter for controlling statement caching, users may find c3p0's alternative maxStatementsPerConnection more intuitive to use.
    public static final String MAX_STATEMENTS_KEY = "maxStatements";
    // The number of PreparedStatements c3p0 will cache for a single pooled Connection.
    // If both maxStatements and maxStatementsPerConnection are zero, statement caching will not be enabled.
    // If maxStatementsPerConnection is zero but maxStatements is a non-zero value, statement caching will be enabled, and a global limit enforced, but otherwise no limit will be set on the number of cached statements for a single Connection.
    // If set, maxStatementsPerConnection should be set to about the number distinct PreparedStatements that are used frequently in your application, plus two or three extra so infrequently statements don't force the more common cached statements to be culled.
    // Though maxStatements is the JDBC standard parameter for controlling statement caching, users may find maxStatementsPerConnection more intuitive to use.
    public static final String MAX_STATEMENTS_PRE_CONNECTION = "maxStatementsPerConnection";
    // Seconds a Connection can remain pooled but unused before being discarded. Zero means idle connections never expire.
    public static final String MAX_IDLE_TIME_KEY = "maxIdleTime";
    // Defines the query that will be executed for all connection tests, if the default ConnectionTester (or some other implementation of QueryConnectionTester, or better yet FullQueryConnectionTester) is being used.
    // Defining a preferredTestQuery that will execute quickly in your database may dramatically speed up Connection tests.
    // (If no preferredTestQuery is set, the default ConnectionTester executes a getTables() call on the Connection's DatabaseMetaData.
    // Depending on your database, this may execute more slowly than a "normal" database query.) NOTE: The table against which your preferredTestQuery will be run must exist in the database schema prior to your initialization of your DataSource.
    // If your application defines its own schema, try automaticTestTable instead.
    public static final String PREFERRED_TEST_QUERY_KEY = "preferredTestQuery";
    // If this is a number greater than 0, c3p0 will test all idle, pooled but unchecked-out connections, every this number of seconds.
    public static final String IDLE_CONNECTION_TEST_PERIOD_KEY = "idleConnectionTestPeriod";
    // If true, an operation will be performed asynchronously at every connection checkin to verify that the connection is valid.
    // Use in combination with idleConnectionTestPeriod for quite reliable, always asynchronous Connection testing.
    // Also, setting an automaticTestTable or preferredTestQuery will usually speed up all connection tests.
    public static final String TEST_CONNECTION_ON_CHECKIN_KEY = "testConnectionOnCheckin";
    // Seconds, effectively a time to live.
    // A Connection older than maxConnectionAge will be destroyed and purged from the pool.
    // This differs from maxIdleTime in that it refers to absolute age.
    // Even a Connection which has not been much idle will be purged from the pool if it exceeds maxConnectionAge.
    // Zero means no maximum absolute age is enforced.
    public static final String MAX_CONNECTION_AGE_KEY = "maxConnectionAge";
    // Number of seconds that Connections in excess of minPoolSize should be permitted to remain idle in the pool before being culled.
    // Intended for applications that wish to aggressively minimize the number of open Connections, shrinking the pool back towards minPoolSize if, following a spike, the load level diminishes and Connections acquired are no longer needed.
    // If maxIdleTime is set, maxIdleTimeExcessConnections should be smaller if the parameter is to have any effect. Zero means no enforcement, excess Connections are not idled out.
    public static final String MAX_IDLE_TIME_EXCESS_CONNECTIONS_KEY = "maxIdleTimeExcessConnections";
    // Maximum time in seconds before user configuration constraints are enforced.
    // Determines how frequently maxConnectionAge, maxIdleTime, maxIdleTimeExcessConnections, unreturnedConnectionTimeout are enforced.
    // c3p0 periodically checks the age of Connections to see whether they've timed out.
    // This parameter determines the period. Zero means automatic: A suitable period will be determined by c3p0.
    // [You can call getEffectivePropertyCycle...() methods on a c3p0 PooledDataSource to find the period automatically chosen.]
    public static final String PROPERTY_CYCLE_KEY = "propertyCycle";
    // If true, an operation will be performed at every connection checkout to verify that the connection is valid. Be sure to set an efficient preferredTestQuery or automaticTestTable if you set this to true.
    // Performing the (expensive) default Connection test on every client checkout will harm client performance.
    // Testing Connections in checkout is the simplest and most reliable form of Connection testing, but for better performance, consider verifying connections periodically using idleConnectionTestPeriod.
    public static final String TEST_CONNECTION_ON_CHECKOUT_KEY = "testConnectionOnCheckout";
    // Seconds. If set, if an application checks out but then fails to check in [i.e. close()] a Connection within the specified period of time, the pool will unceremoniously destroy() the Connection.
    // This permits applications with occasional Connection leaks to survive, rather than eventually exhausting the Connection pool.
    // And that's a shame. Zero means no timeout, applications are expected to close() their own Connections. Obviously, if a non-zero value is set, it should be to a value longer than any Connection should reasonably be checked-out.
    // Otherwise, the pool will occasionally kill Connections in active use, which is bad. This is basically a bad idea, but it's a commonly requested feature. Fix your $%!@% applications, so they don't leak Connections!
    // Use this temporarily in combination with debugUnreturnedConnectionStackTraces to figure out where Connections are being checked-out that don't make it back into the pool!
    public static final String UNRETURNED_CONNECTION_TIMEOUT_KEY = "unreturnedConnectionTimeout";

    public static final String DEFAULT_TESTER_CLASS_NAME = "com.mchange.v2.c3p0.impl.DefaultConnectionTester";
}
