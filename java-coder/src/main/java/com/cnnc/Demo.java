package com.cnnc;

class A {
    A b;
    public A() {
        this.b = this;
    }
}

public class Demo {

//    public static int f(int i) {
//        if (i <= 1) {
//            return 1;
//        }
//        return i * f(i - 1);
//    }

    public static void main(String[] args) {

//        System.out.println(f(5));
//        int a = 1;
//        a();
        A a = new A();
        System.out.println(a);
        System.out.println(a.b);
        a = new A();
        System.out.println(a);
        System.out.println(a.b);
    }

    static void a() {
        b();
    }

    static void b() {
        c();
    }

    static void c() {

    }

}
