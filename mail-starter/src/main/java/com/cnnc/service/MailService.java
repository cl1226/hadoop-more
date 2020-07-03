package com.cnnc.service;

import com.cnnc.config.MailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailService {
    @Autowired
    private MailProperties properties;

    public String say(String name) {
        return "Hello " + name + " " + properties.getMsg();
    }
}
