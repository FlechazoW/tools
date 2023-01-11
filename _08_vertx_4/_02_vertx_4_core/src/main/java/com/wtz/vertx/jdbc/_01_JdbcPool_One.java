package com.wtz.vertx.jdbc;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCConnectOptions;
import io.vertx.jdbcclient.JDBCConnectOptionsConverter;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.PoolOptionsConverter;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import static com.wtz.vertx.jdbc.C3P0Constants.DEFAULT_TESTER_CLASS_NAME;
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
import static com.wtz.vertx.jdbc.C3P0Constants.PREFERRED_TEST_QUERY_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.PROPERTY_CYCLE_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.TEST_CONNECTION_ON_CHECKIN_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.TEST_CONNECTION_ON_CHECKOUT_KEY;
import static com.wtz.vertx.jdbc.C3P0Constants.UNRETURNED_CONNECTION_TIMEOUT_KEY;
import static com.wtz.vertx.jdbc.VertxJdbcClientConstant.CONNECTION_TIMEOUT_KEY;
import static com.wtz.vertx.jdbc.VertxJdbcClientConstant.CONNECTION_TIMEOUT_UNIT_KEY;
import static com.wtz.vertx.jdbc.VertxJdbcClientConstant.DATABASE_KEY;
import static com.wtz.vertx.jdbc.VertxJdbcClientConstant.EVENT_LOOP_SIZE_KEY;
import static com.wtz.vertx.jdbc.VertxJdbcClientConstant.IDLE_TIMEOUT_KEY;
import static com.wtz.vertx.jdbc.VertxJdbcClientConstant.JDBC_URL_KEY;
import static com.wtz.vertx.jdbc.VertxJdbcClientConstant.MAX_SIZE_KEY;
import static com.wtz.vertx.jdbc.VertxJdbcClientConstant.MAX_WAIT_QUEUE_SIZE_KEY;
import static com.wtz.vertx.jdbc.VertxJdbcClientConstant.METRICS_ENABLED_KEY;
import static com.wtz.vertx.jdbc.VertxJdbcClientConstant.PASSWORD_KEY;
import static com.wtz.vertx.jdbc.VertxJdbcClientConstant.POOL_CLEANER_PERIOD_KEY;
import static com.wtz.vertx.jdbc.VertxJdbcClientConstant.SHARED_KEY;
import static com.wtz.vertx.jdbc.VertxJdbcClientConstant.TRACING_POLICY_KEY;
import static com.wtz.vertx.jdbc.VertxJdbcClientConstant.USER_NAME_KEY;

public class _01_JdbcPool_One {

    public static void main(String[] args) throws SQLException {
        Map<String, Object> props = new HashMap<>();

        props.put("driverName", "oracle.jdbc.driver.OracleDriver");
        props.put("url", "jdbc:oracle:thin:@172.16.100.243:1521:ORCL");
        props.put("tableName", "TEST_TWO");
        props.put("schema", "TIEZHU");
        props.put("username", "tiezhu");
        props.put("password", "abc123");

        RdbSideTableInfo tableInfo = new RdbSideTableInfo();
        set(props, tableInfo);

        tableInfo.setSqlCondition("SELECT \"ID\", \"CLOB_VAL\", \"NCLOB_VAL\", \"BLOB_VAL\" FROM \"TIEZHU\".\"DIM_CLOB_BLOB\" WHERE \"ID\" = ? AND DBMS_LOB.COMPARE(\"CLOB_VAL\", TO_CLOB(?)) = 0 AND DBMS_LOB.INSTR(\"BLOB_VAL\", UTL_RAW.CAST_TO_RAW(?), 1, 1) > 0");

        JsonObject entries = JsonObject.mapFrom(tableInfo);

        DTC3P0DataSourceProvider provider = new DTC3P0DataSourceProvider();
        DataSource source = provider.getDataSource(entries);

        Vertx vertx = Vertx.vertx();
        JDBCPool pool = JDBCPool.pool(vertx, source);
        List<Object> list = new LinkedList<>();
        list.add(1);
        list.add("dd");
        list.add("foo");

        Tuple tuple = Tuple.tuple();
        for (Object o : list) {
            tuple.addValue(o);
        }
        pool
                .preparedQuery(tableInfo.getSqlCondition())
                .execute(tuple)
                .onFailure(System.out::println)
                .onSuccess(result -> {
                    for (Row row : result) {
                        for (int i = 0; i < row.size(); i++) {
                            System.out.println(row.getValue(i));
                        }
                    }
                });
    }


    public static JDBCConnectOptions buildVertxJdbcConnectOptions(RdbSideTableInfo tableInfo) {
        JDBCConnectOptions options = new JDBCConnectOptions();
        JsonObject entries = new JsonObject();
        entries.put(JDBC_URL_KEY, tableInfo.getUrl())
                .put(USER_NAME_KEY, tableInfo.getUserName())
                .put(PASSWORD_KEY, tableInfo.getPassword())
                .put(DATABASE_KEY, tableInfo.getSchema())
                .put(IDLE_TIMEOUT_KEY, tableInfo.getIdleTimeout())
                .put(METRICS_ENABLED_KEY, tableInfo.isMetricsEnabled())
                .put(TRACING_POLICY_KEY, tableInfo.getTracingPolicy());
        JDBCConnectOptionsConverter.fromJson(entries, options);
        return options;
    }

    public static PoolOptions buildVertxPoolOptions(RdbSideTableInfo tableInfo) {
        PoolOptions options = new PoolOptions();
        JsonObject entries = new JsonObject();
        entries.put(CONNECTION_TIMEOUT_KEY, tableInfo.getConnectionTimeout())
                .put(CONNECTION_TIMEOUT_UNIT_KEY, tableInfo.getConnectionTimeoutUnit())
                .put(EVENT_LOOP_SIZE_KEY, tableInfo.getEventLoopSize())
                .put(MAX_SIZE_KEY, tableInfo.getMaxSize())
                .put(MAX_WAIT_QUEUE_SIZE_KEY, tableInfo.getMaxWaitQueueSize())
                .put(POOL_CLEANER_PERIOD_KEY, tableInfo.getPoolCleanerPeriod())
                .put(SHARED_KEY, tableInfo.isShared());
        PoolOptionsConverter.fromJson(entries, options);
        return options;
    }

    public static void set(Map<String, Object> props, RdbSideTableInfo rdbTableInfo) {
        rdbTableInfo.setUrl(MathUtil.getString(props.get(RdbSideTableInfo.URL_KEY.toLowerCase())));
        rdbTableInfo.setTableName(MathUtil.getString(props.get(RdbSideTableInfo.TABLE_NAME_KEY.toLowerCase())));
        rdbTableInfo.setUserName(MathUtil.getString(props.get(RdbSideTableInfo.USER_NAME_KEY.toLowerCase())));
        rdbTableInfo.setPassword(MathUtil.getString(props.get(RdbSideTableInfo.PASSWORD_KEY.toLowerCase())));
        rdbTableInfo.setSchema(MathUtil.getString(props.get(RdbSideTableInfo.SCHEMA_KEY.toLowerCase())));
        rdbTableInfo.setDriverName(MathUtil.getString(props.get(RdbSideTableInfo.DRIVER_NAME)));
        // c3p0 parameters
        rdbTableInfo.setAcquireIncrement(MathUtil.getIntegerVal(props.getOrDefault(ACQUIRE_INCREMENT_KEY.toLowerCase(), 3)));
        rdbTableInfo.setAcquireRetryAttempts(MathUtil.getIntegerVal(props.getOrDefault(ACQUIRE_RETRY_ATTEMPTS_KEY.toLowerCase(), 30)));
        rdbTableInfo.setAcquireRetryDelay(MathUtil.getIntegerVal(props.getOrDefault(ACQUIRE_RETRY_DELAY_KEY.toLowerCase(), 1000)));
        rdbTableInfo.setAutoCommitOnClose(MathUtil.getBoolean(props.getOrDefault(AUTO_COMMIT_ON_CLOSE_KEY.toLowerCase(), false)));
        rdbTableInfo.setAutomaticTestTable(MathUtil.getString(props.getOrDefault(AUTOMATIC_TEST_TABLE_KEY.toLowerCase(), null)));
        rdbTableInfo.setBreakAfterAcquireFailure(MathUtil.getBoolean(props.getOrDefault(BREAK_AFTER_ACQUIRE_FAILURE_KEY.toLowerCase(), false)));
        rdbTableInfo.setCheckoutTimeout(MathUtil.getIntegerVal(props.getOrDefault(CHECKOUT_TIMEOUT_KEY.toLowerCase(), 0)));
        rdbTableInfo.setConnectionCustomizerClassName(MathUtil.getString(props.getOrDefault(CONNECTION_CUSTOMIZER_CLASS_NAME.toLowerCase(), null)));
        rdbTableInfo.setConnectionTesterClassName(MathUtil.getString(props.getOrDefault(CONNECTION_TESTER_CLASS_NAME.toLowerCase(), DEFAULT_TESTER_CLASS_NAME)));
        rdbTableInfo.setDebugUnreturnedConnectionStackTraces(MathUtil.getBoolean(props.getOrDefault(DEBUG_UNRETURNED_CONNECTION_STACK_TRACES_KEY.toLowerCase(), false)));
        rdbTableInfo.setForceIgnoreUnresolvedTransactions(MathUtil.getBoolean(props.getOrDefault(FORCE_IGNORE_UNRESOLVED_TRANSACTIONS_KEY.toLowerCase(), false)));
        rdbTableInfo.setForceSynchronousCheckins(MathUtil.getBoolean(props.getOrDefault(FORCE_SYNCHRONOUS_CHECKINS_KEY.toLowerCase(), false)));
        rdbTableInfo.setIdleConnectionTestPeriod(MathUtil.getIntegerVal(props.getOrDefault(IDLE_CONNECTION_TEST_PERIOD_KEY.toLowerCase(), 0)));
        rdbTableInfo.setInitialPoolSize(MathUtil.getIntegerVal(props.getOrDefault(INITIAL_POOL_SIZE_KEY.toLowerCase(), 3)));
        rdbTableInfo.setMaxConnectionAge(MathUtil.getIntegerVal(props.getOrDefault(MAX_CONNECTION_AGE_KEY.toLowerCase(), 0)));
        rdbTableInfo.setMaxIdleTime(MathUtil.getIntegerVal(props.getOrDefault(MAX_IDLE_TIME_KEY.toLowerCase(), 0)));
        rdbTableInfo.setMaxIdleTimeExcessConnections(MathUtil.getIntegerVal(props.getOrDefault(MAX_IDLE_TIME_EXCESS_CONNECTIONS_KEY.toLowerCase(), 0)));
        rdbTableInfo.setMaxPoolSize(MathUtil.getIntegerVal(props.getOrDefault(MAX_POOL_SIZE_KEY.toLowerCase(), 15)));
        rdbTableInfo.setMaxStatements(MathUtil.getIntegerVal(props.getOrDefault(MAX_STATEMENTS_KEY.toLowerCase(), 0)));
        rdbTableInfo.setMaxStatementsPerConnection(MathUtil.getIntegerVal(props.getOrDefault(MAX_STATEMENTS_PRE_CONNECTION.toLowerCase(), 0)));
        rdbTableInfo.setMinPoolSize(MathUtil.getIntegerVal(props.getOrDefault(MIN_POOL_SIZE_KEY.toLowerCase(), 3)));
        rdbTableInfo.setPreferredTestQuery(MathUtil.getString(props.getOrDefault(PREFERRED_TEST_QUERY_KEY.toLowerCase(), "")));
        rdbTableInfo.setPropertyCycle(MathUtil.getIntegerVal(props.getOrDefault(PROPERTY_CYCLE_KEY.toLowerCase(), 0)));
        rdbTableInfo.setTestConnectionOnCheckin(MathUtil.getBoolean(props.getOrDefault(TEST_CONNECTION_ON_CHECKIN_KEY.toLowerCase(), false)));
        rdbTableInfo.setTestConnectionOnCheckout(MathUtil.getBoolean(props.getOrDefault(TEST_CONNECTION_ON_CHECKOUT_KEY.toLowerCase(), false)));
        rdbTableInfo.setUnreturnedConnectionTimeout(MathUtil.getIntegerVal(props.getOrDefault(UNRETURNED_CONNECTION_TIMEOUT_KEY.toLowerCase(), 0)));

        // vertx sql client parameters
        rdbTableInfo.setIdleTimeout(MathUtil.getIntegerVal(props.getOrDefault(IDLE_TIMEOUT_KEY.toLowerCase(), 60 * 1000)));
        rdbTableInfo.setMetricsEnabled(MathUtil.getBoolean(props.getOrDefault(METRICS_ENABLED_KEY.toLowerCase(), false)));
        rdbTableInfo.setTracingPolicy(MathUtil.getString(props.getOrDefault(TRACING_POLICY_KEY.toLowerCase(), "PROPAGATE")));
        rdbTableInfo.setConnectionTimeout(MathUtil.getIntegerVal(props.getOrDefault(CONNECTION_TIMEOUT_KEY.toLowerCase(), 30)));
        rdbTableInfo.setConnectionTimeoutUnit(MathUtil.getString(props.getOrDefault(CONNECTION_TIMEOUT_UNIT_KEY, TimeUnit.SECONDS.name())));
        rdbTableInfo.setEventLoopSize(MathUtil.getIntegerVal(props.getOrDefault(EVENT_LOOP_SIZE_KEY, 0)));
        rdbTableInfo.setMaxSize(MathUtil.getIntegerVal(props.getOrDefault(MAX_SIZE_KEY, 4)));
        rdbTableInfo.setMaxWaitQueueSize(MathUtil.getIntegerVal(props.getOrDefault(MAX_WAIT_QUEUE_SIZE_KEY, -1)));
        rdbTableInfo.setPoolCleanerPeriod(MathUtil.getIntegerVal(props.getOrDefault(POOL_CLEANER_PERIOD_KEY, 1000)));
        rdbTableInfo.setShared(MathUtil.getBoolean(props.getOrDefault(SHARED_KEY, false)));
    }
}
