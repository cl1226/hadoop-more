package com.cnnc.asm;

public class Car implements Movable {
    @Override
    public String run() {
        System.out.println("running...");
        return "running";
    }
}
