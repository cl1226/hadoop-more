package com.cnnc.spi;

public class HDFSServiceImpl implements IService {
    @Override
    public String getSchema() {
        return "HDFS";
    }

    @Override
    public void say() {
        System.out.println("hdfs");
    }
}
