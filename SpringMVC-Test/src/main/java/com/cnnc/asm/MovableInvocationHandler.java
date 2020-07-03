package com.cnnc.asm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MovableInvocationHandler implements InvocationHandler {

    private Movable movable;

    public MovableInvocationHandler(Movable movable) {
        this.movable = movable;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before...");
        Object invoke = method.invoke(movable, args);
        System.out.println("after...");
        return invoke;
    }
}
