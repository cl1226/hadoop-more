package com.cnnc.inner;

import java.lang.ref.WeakReference;

public class Car {

    private String name;

    public Car(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("finalize");
    }

    public static void main(String[] args) {
        Car car = new Car("aaa");
        WeakReference<Car> ref = new WeakReference<>(car);

        while (true) {
            if (ref.get() != null) {
                System.out.println("car exists");
            } else {
                System.out.println("car has been gc");
                break;
            }
        }

        System.out.println(ref.get());
    }
}
