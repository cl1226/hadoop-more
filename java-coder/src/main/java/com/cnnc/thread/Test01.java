package com.cnnc.thread;

import static com.cnnc.thread.Constants.*;

public class Test01 {

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Task(), "Thread-01");

        Thread t2 = new Thread(new Task(), "Thread-02");

        t1.start();
        t2.start();

        Thread.sleep(1000);

        flag = false;

        System.out.println(atomic.get());

    }


}
