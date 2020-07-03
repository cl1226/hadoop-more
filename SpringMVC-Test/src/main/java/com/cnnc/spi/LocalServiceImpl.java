package com.cnnc.spi;

public class LocalServiceImpl implements IService {
    @Override
    public String getSchema() {
        return "LOCAL";
    }

    @Override
    public void say() {
        System.out.println("LOCAL");
    }
}
