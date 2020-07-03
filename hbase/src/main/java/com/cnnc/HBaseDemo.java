package com.cnnc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HBaseDemo {

    private Configuration configuration = null;
    private Connection connection = null;
    private Admin admin = null;
    private TableName tableName = TableName.valueOf("test");
    private Table table = null;

    @Before
    public void init() throws Exception {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "node01");
        connection = ConnectionFactory.createConnection(configuration);
        admin = connection.getAdmin();
        table = connection.getTable(tableName);
    }

    @Test
    public void createTable() throws IOException {
        TableDescriptorBuilder table = TableDescriptorBuilder.newBuilder(tableName);
        ColumnFamilyDescriptorBuilder cf = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("cf"));
        cf.setMaxVersions(4);
        cf.setMinVersions(2);
        cf.setTimeToLive(60);
        table.setColumnFamily(cf.build());

        if (admin.tableExists(tableName)) {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        }
        admin.createTable(table.build());
    }

    @Test
    public void createTable2() throws IOException {
        TableName tableName1 = TableName.valueOf("test");
        TableDescriptorBuilder table = TableDescriptorBuilder.newBuilder(tableName1);
        ColumnFamilyDescriptorBuilder cf = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("cf1"));
        table.setColumnFamily(cf.build());

        ColumnFamilyDescriptorBuilder cf2 = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("cf2"));
        table.setColumnFamily(cf2.build());

        if (admin.tableExists(tableName1)) {
            admin.disableTable(tableName1);
            admin.deleteTable(tableName1);
        }
        admin.createTable(table.build());
    }

    @Test
    public void insert() throws IOException {
        List<Put> puts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Put put = new Put(Bytes.toBytes("row" + i));
            put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes("zhangsan" + i));
            put.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("aaa"), Bytes.toBytes("aaa" + i));
            puts.add(put);
        }

        table.put(puts);
    }

    @Test
    public void get() throws IOException {
        Get get = new Get(Bytes.toBytes("row1"));
        get.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("name"));
        Result result = table.get(get);
        List<Cell> cells = result.listCells();
        for (Cell cell : cells) {
            String res = Bytes.toString(cell.getValueArray(),
                    cell.getValueOffset(),
                    cell.getValueLength());
            System.out.println("res:" + res + " timestamp:" + cell.getTimestamp());
        }
    }

    @Test
    public void scan() throws IOException {
        Scan scan = new Scan();
//        scan.setMaxVersions(10);
//        scan.setCaching(1);
        scan.setBatch(1);
        ResultScanner rss = table.getScanner(scan);
        Result next = rss.next();
        while (next != null) {
            System.out.println(next.listCells());
            next = rss.next();
        }
//        for (Result rs : rss) {
//            List<Cell> cells = rs.listCells();
//            for (Cell cell : cells) {
//                String res = Bytes.toString(cell.getValueArray(),
//                        cell.getValueOffset(),
//                        cell.getValueLength());
//                System.out.println("res:" + res + " timestamp:" + cell.getTimestamp());
//            }
////            Cell cell1 = rs.getColumnLatestCell(Bytes.toBytes("cf"),Bytes.toBytes("name"));
////            String name = Bytes.toString(CellUtil.cloneValue(cell1));
////            System.out.println(name);
//        }
    }

    @After
    public void destory() throws IOException {
        connection.close();
        admin.close();
    }

    @Test
    public void ping() {

    }

}
