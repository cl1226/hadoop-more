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

public class HBaseDemo {

    private Configuration configuration = null;
    private Connection connection = null;
    private Admin admin = null;
    private TableName tableName = TableName.valueOf("phone");
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
        TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableName);
        ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("cf"));
        tableDescriptorBuilder.setColumnFamily(columnFamilyDescriptorBuilder.build());
        if (admin.tableExists(tableName)) {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        }
        admin.createTable(tableDescriptorBuilder.build());
    }

    @Test
    public void insert() throws IOException {
        Put put = new Put(Bytes.toBytes("1111"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("name"), Bytes.toBytes("zhangsan"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("age"), Bytes.toBytes("16"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("sex"), Bytes.toBytes("women"));
        table.put(put);
    }

    @Test
    public void get() throws IOException {
        Get get = new Get(Bytes.toBytes("2222"));
        get.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("name"));
        Result result = table.get(get);
        Cell cell = result.getColumnLatestCell(Bytes.toBytes("cf"), Bytes.toBytes("name"));
        String name = Bytes.toString(CellUtil.cloneValue(cell));
        System.out.println(name);
    }

    @Test
    public void scan() throws IOException {
        Scan scan = new Scan();
        ResultScanner rss = table.getScanner(scan);
        for (Result rs : rss) {
            Cell cell1 = rs.getColumnLatestCell(Bytes.toBytes("cf"),Bytes.toBytes("name"));
            Cell cell2 = rs.getColumnLatestCell(Bytes.toBytes("cf"),Bytes.toBytes("age"));
            Cell cell3 = rs.getColumnLatestCell(Bytes.toBytes("cf"),Bytes.toBytes("sex"));
            String name = Bytes.toString(CellUtil.cloneValue(cell1));
            String age = Bytes.toString(CellUtil.cloneValue(cell2));
            String sex = Bytes.toString(CellUtil.cloneValue(cell3));
            System.out.println(name);
            System.out.println(age);
            System.out.println(sex);
        }
    }

    @After
    public void destory() throws IOException {
        connection.close();
        admin.close();
    }

}
