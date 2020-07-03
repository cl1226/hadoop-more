package com.cnnc.controller;

import com.cnnc.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    MailService mailService;

    @RequestMapping("/test")
    public String test(String name) {
        return mailService.say(name);
    }

}
