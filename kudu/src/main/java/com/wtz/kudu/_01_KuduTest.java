package com.wtz.kudu;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.CreateTableOptions;
import org.apache.kudu.client.DeleteTableResponse;
import org.apache.kudu.client.Insert;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.apache.kudu.client.KuduScanner;
import org.apache.kudu.client.KuduSession;
import org.apache.kudu.client.KuduTable;
import org.apache.kudu.client.OperationResponse;
import org.apache.kudu.client.PartialRow;
import org.apache.kudu.client.RowResult;
import org.apache.kudu.client.RowResultIterator;
import org.apache.kudu.client.SessionConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tiezhu@dtstack.com
 * @since 2022/1/20 星期四
 */
public class _01_KuduTest {
    public static void main(String[] args) throws KuduException {
        String kuduMaster = "172.16.100.109:7051";
        String tableName = "tiezhuDemoOne";
        KuduClient kuduClient = buildKuduClient(kuduMaster);
        printTableServer(kuduClient);
//        createKuduTable(tableName, kuduClient);
        List<String> tablesList = kuduClient.getTablesList().getTablesList();
        System.out.println("------------ table name ------------");
        tablesList.forEach(System.out::println);
        insertDataIntoTable(
                10000,
                1000,
                tableName,
                kuduClient
        );
        printDataFromTable(
                tableName,
                kuduClient,
                "id", "name", "age"
        );

//        deleteTable(tableName, kuduClient);
    }

    /**
     * build kudu client
     *
     * @param kuduMaster the kudu master address
     * @return active client
     */
    private static KuduClient buildKuduClient(String kuduMaster) {
        if (Objects.isNull(kuduMaster)) {
            throw new NullPointerException("kudu master can not be null");
        }

        // 可以修改些默认配置
        return new KuduClient.KuduClientBuilder(kuduMaster).build();
    }

    /**
     * print the list of table server
     *
     * @param client active kudu client
     * @throws KuduException kudu exception when get table server
     */
    private static void printTableServer(KuduClient client) throws KuduException {
        List<String> tabletServersList = client.listTabletServers().getTabletServersList();
        if (Objects.nonNull(tabletServersList)) {
            System.out.println("Print kudu table Server:");
            tabletServersList.forEach(System.out::println);
        }
    }

    /**
     * create kudu table with client
     *
     * @param tableName table name
     * @param client    kudu client
     * @throws KuduException kudu exception when create table
     */
    private static void createKuduTable(String tableName, KuduClient client) throws KuduException {
        // table exist
        if (client.tableExists(tableName)) {
            throw new IllegalArgumentException(
                    String.format("table [%s] already exist!", tableName)
            );
        }

        // create table columns
        List<ColumnSchema> columnSchemas = new ArrayList<>();

        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("id", Type.STRING).key(true).build());
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("name", Type.STRING).key(true).build());
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("age", Type.INT32).key(true).build());

        CreateTableOptions options = new CreateTableOptions();
        List<String> partitionList = new ArrayList<>();

        partitionList.add("id");
        // partitionNumber = partition.hashcode % partitionCount
        options.addHashPartitions(partitionList, 16);
        options.setRangePartitionColumns(partitionList);

        // replicas of data
        options.setNumReplicas(1);

        Schema schema = new Schema(columnSchemas);

        client.createTable(tableName, schema, options);
    }


    /**
     * insert data into table by kudu client
     *
     * @param insertNum the count number of insert data
     * @param flushSize the size of opts
     * @param tableName insert table name
     * @param client    kudu client
     * @throws KuduException kudu exception when insert kudu data
     */
    private static void insertDataIntoTable(int insertNum, int flushSize, String tableName, KuduClient client) throws KuduException {
        long start = System.currentTimeMillis();
        if (!client.tableExists(tableName)) {
            throw new IllegalArgumentException(
                    String.format("table [%s] not exist!", tableName));
        }
        KuduTable kuduTable = client.openTable(tableName);

        KuduSession kuduSession = client.newSession();

        // 采用MANUAL_FLUSH模式
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.MANUAL_FLUSH);
        // 设置opts的数量  Set the number of operations that can be buffered.
        kuduSession.setMutationBufferSpace(flushSize);
        // 设置下一次opts的超时时间
        // Sets the timeout for the next applied operations.
        // The default timeout is 0, which disables the timeout functionality.
        kuduSession.setTimeoutMillis(3000L);
        try {
            for (int i = 0; i < insertNum; i++) {
                Insert insert = kuduTable.newInsert();
                PartialRow insertRow = insert.getRow();
                insertRow.addString("id", String.valueOf(i));
                insertRow.addString("name",
                        RandomStringUtils.random(8, true, false));
                insertRow.addInt("age", Integer.parseInt(
                        RandomStringUtils.random(2, false, true)));
                kuduSession.apply(insert);
                if (i % flushSize == 0) {
                    List<OperationResponse> flush = kuduSession.flush();
                }
            }
        } finally {
            kuduSession.flush();
        }
        long end = System.currentTimeMillis();
        System.out.println("--------- time ----------");
        double rate = Double.parseDouble(String.valueOf(insertNum / (end - start)));
        System.out.println("当前速率: " + rate * 1000);
    }

    private static void printDataFromTable(String tableName, KuduClient client, String... columnNames) throws KuduException {
        List<String> columns = Arrays.stream(columnNames).collect(Collectors.toList());
        if (!client.tableExists(tableName)) {
            throw new IllegalArgumentException(
                    String.format("table [%s] not exist!", tableName));
        }

        KuduTable kuduTable = client.openTable(tableName);
        KuduScanner rowResults = client.newScannerBuilder(kuduTable).setProjectedColumnNames(columns).build();
        int rowCount = 0;
        try {
            while (rowResults.hasMoreRows()) {
                RowResultIterator results = rowResults.nextRows();
                int numRows = results.getNumRows();
                rowCount += numRows;
                System.out.println("--------- row ----------");
                System.out.println("Get row count: " + numRows);

//                 打印查询结果
                while (results.hasNext()) {
                    RowResult next = results.next();
                    System.out.println("Get row result: " + next.rowToString());
                }
            }
            System.out.println("--------- sum ----------");
            System.out.println("Get total row:" + rowCount);
        } finally {
            rowResults.close();
        }
    }

    private static void deleteTable(String tableName, KuduClient client) throws KuduException {
        if (!client.tableExists(tableName)) {
            throw new IllegalArgumentException(
                    String.format("table [%s] not exist!", tableName)
            );
        }
        DeleteTableResponse deleteTableResponse = client.deleteTable(tableName);
        System.out.println("Delete table response: " + deleteTableResponse.toString());
    }
}
