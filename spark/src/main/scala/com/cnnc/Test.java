package com.cnnc;

import org.apache.commons.lang3.RandomUtils;
import org.apache.parquet.bytes.BytesUtils;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.*;

public class Test {

//    private Long l1;
//
//    static {
//        sun.misc.Unsafe unsafe;
//        try {
//            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
//            theUnsafe.setAccessible(true);
//            unsafe = (Unsafe) theUnsafe.get(null);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            unsafe = null;
//        }
//        int i = unsafe.arrayBaseOffset(long[].class);
//
//        long[] data = new long[1024];
//
//        unsafe.putInt(BytesUtils.intToBytes(234), i ,3);
//
//    }

    public static void main(String[] args) throws InterruptedException {
//        PriorityQueue<Object> priorityQueue = new PriorityQueue<>();
//
//        priorityQueue.add("c");
//        priorityQueue.add("a");
//        priorityQueue.add("e");
//        priorityQueue.add("b");
//        priorityQueue.add("d");
//
//        while (!priorityQueue.isEmpty()) {
//            System.out.println(priorityQueue.poll());
//        }

//        SynchronousQueue<Object> q = new SynchronousQueue<>();
//        new Thread(() -> {
//            try {
//                System.out.println(q.take());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        q.put("sss");
//
//        System.out.println(q.size());

//        LinkedTransferQueue<Object> transferQueue = new LinkedTransferQueue<>();
//
//
//        new Thread(() -> {
//            try {
//                System.out.println(transferQueue.take());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        transferQueue.transfer("aaa");

        LinkedBlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();

//        Thread producer = new Thread(() -> {
//            int i = 0;
//            while (true) {
//                System.out.println("produce ---" + i);
//                blockingQueue.offer(i++);
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        });
//        producer.start();
//
//
//        Thread consumer = new Thread(() -> {
//            while (true) {
//                Integer poll = blockingQueue.poll();
//                System.out.println("Consume ---" + poll);
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        consumer.start();
//
//        producer.join();
//        consumer.join();

//        ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<>(1);
//        arrayBlockingQueue.put(1);
//        arrayBlockingQueue.put(2);

        char[] a = new char[]{'1','2','3','4','5'};
        char[] b = new char[]{'a', 'b', 'c', 'd', 'e'};

        Exchanger<String> exchanger = new Exchanger<>();

        new Thread(() -> {
            for (int i = 0; i < a.length; i++) {
                System.out.println(a[i]);
                try {
                    exchanger.exchange("A");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < b.length; i++) {

                try {
                    exchanger.exchange("B");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(b[i]);
            }
        }).start();


    }

}
