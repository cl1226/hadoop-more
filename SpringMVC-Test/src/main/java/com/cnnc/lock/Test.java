package com.cnnc.lock;

public class Test {

    private int m = 10;

    public synchronized int getM() {
        return m;
    }

    public synchronized void m(int a) throws InterruptedException {
        Thread.sleep(5000);
        System.out.println(a);
    }

    public synchronized void m2(int a) throws InterruptedException {
        Thread.sleep(5000);
        System.out.println(a);
    }

    public static void main(String[] args) throws InterruptedException {
        Test test = new Test();
        Thread thread = new Thread(() -> {
            try {
                System.out.println("a");
                test.m(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        Thread thread1 = new Thread(() -> {
            try {
                System.out.println("b");
                System.out.println(test.getM());
                test.m2(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
    }

}
