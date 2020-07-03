package com.cnnc.springbootmybatis;

public class Test {

    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("123");
        System.out.println(threadLocal.get());
    }

}
