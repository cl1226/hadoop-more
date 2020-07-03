package com.cnnc.asm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Test {

    public static void main(String[] args) {
        Movable movable = new Car();
        Movable proxy = (Movable) Proxy.newProxyInstance(Car.class.getClassLoader(),
                Car.class.getInterfaces(), new MovableInvocationHandler(movable));
        String s = proxy.run();
        System.out.println(proxy.getClass());
    }

}
