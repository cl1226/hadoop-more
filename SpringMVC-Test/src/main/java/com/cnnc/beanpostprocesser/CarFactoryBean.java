package com.cnnc.beanpostprocesser;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component(value = "car")
public class CarFactoryBean implements FactoryBean<Car> {

    @Override
    public Car getObject() throws Exception {
        Car car = new Car();
        car.setColor("red");
        car.setPrice(BigDecimal.valueOf(100));
        return car;
    }

    @Override
    public Class<?> getObjectType() {
        return Car.class;
    }

    @Override
    public String toString() {
        return "CarFactoryBean{}";
    }
}
