package com.cnnc.inner;

public class Outer {

    private int a;

    public Outer() {
        System.out.println("outer init");
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public class Inner {
        private int b;

        public Inner() {
            System.out.println("inner init");
            System.out.println(a);
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }

}
