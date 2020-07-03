package com.cnnc.datastruct;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Test {

    public static void main(String[] args) throws InterruptedException {

        Map<String, String> hashMap = new HashMap<>();

        Map<String, String> concurrentHashMap = new ConcurrentHashMap<>();


        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                concurrentHashMap.put("key" + i, "valuea" + i);
            }
        });


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                concurrentHashMap.put("key" + i, "valueb" + i);
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(concurrentHashMap.size());

        concurrentHashMap.putIfAbsent("k1", "111");

        System.out.println(concurrentHashMap);
    }

}
