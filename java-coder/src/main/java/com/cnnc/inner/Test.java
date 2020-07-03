package com.cnnc.inner;

import java.lang.ref.WeakReference;

public class Test {

    public static void main(String[] args) {
        Outer outer = new Outer();
//        Outer.Inner inner = outer.new Inner();

        WeakReference<Outer> weak = new WeakReference<>(outer);

        System.gc();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Outer outer1 = weak.get();
        System.out.println(outer1);
    }

}
