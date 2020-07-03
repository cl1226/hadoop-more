package com.cnnc.springbootmybatis.datasource;

import com.cnnc.springbootmybatis.config.DBTypeEnum;

import java.util.concurrent.atomic.AtomicInteger;

public class DBContextHolder {

    private static final ThreadLocal<DBTypeEnum> contextHolder = new ThreadLocal<>();

    private static final AtomicInteger counter = new AtomicInteger(-1);

    public static void set(DBTypeEnum dbType) {
        contextHolder.set(dbType);
    }

    public static DBTypeEnum get() {
        DBTypeEnum dbTypeEnum = contextHolder.get();
        System.out.println("决定的数据源类型: " + dbTypeEnum);
        return dbTypeEnum;
    }

    public static void master() {
        set(DBTypeEnum.MASTER);
        System.out.println("切换到master");
    }

    public static void slave() {
        set(DBTypeEnum.SLAVE);
        System.out.println("切换到slave");
    }

}
