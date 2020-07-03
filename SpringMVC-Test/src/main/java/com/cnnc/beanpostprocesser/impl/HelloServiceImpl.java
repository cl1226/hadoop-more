package com.cnnc.beanpostprocesser.impl;

import com.cnnc.beanpostprocesser.HelloService;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello() {
        System.out.println("你好我是HelloServiceImpl1");
    }
}
