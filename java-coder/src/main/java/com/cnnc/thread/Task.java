package com.cnnc.thread;

import static com.cnnc.thread.Constants.*;

public class Task implements Runnable {

    @Override
    public void run() {
        while (flag) {
//            int i = atomic.incrementAndGet();
            System.out.println(Thread.currentThread().getName() + "-----,atomic: " + vol++);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
