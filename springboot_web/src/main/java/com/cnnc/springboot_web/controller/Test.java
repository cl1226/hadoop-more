package com.cnnc.springboot_web.controller;

import com.cnnc.springboot_web.listener.MyListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @RequestMapping(name = "C1", path = "test", method = RequestMethod.GET)
    public String test01() {
        return "test01";
    }

    @GetMapping("online")
    public String online() {
        return "当前在线人数: " + MyListener.online;
    }
}
