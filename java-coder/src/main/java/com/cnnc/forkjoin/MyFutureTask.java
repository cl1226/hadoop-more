package com.cnnc.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MyFutureTask {

    public static String doSomethingA() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("--- doSomethingA---");

        return "TaskAResult";//返回结果
    }

    public static String doSomethingB() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("--- doSomethingB---");
        return "TaskBResult";

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        long start = System.currentTimeMillis();
        int result = 1;
        FutureTask<Integer> task1 = new FutureTask<>(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int i = 0;
            i = i+10;
            return i;
        });
        Thread thread1 = new Thread(task1, "task1");
        thread1.start();
        Integer res = task1.get();
        System.out.println(res);

//        FutureTask<String> futureTask = new FutureTask<String>(() -> {
//            String result = doSomethingA();
//            return result;
//        });
//        Thread thread = new Thread(futureTask, "threadA");
//        thread.start();
//
//        String taskBResult = doSomethingB();
//
//        String taskAResult = futureTask.get();
//        System.out.println(taskAResult + " " + taskBResult);
//        System.out.println(System.currentTimeMillis() - start);

    }

}
