package com.cnnc.springbootmybatis.controller;

import com.cnnc.springbootmybatis.domain.User;
import com.cnnc.springbootmybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/get")
    public String getUser(@RequestParam Long id) {

        User user = (User) userService.get(id);

        return user.toString();
    }

    @GetMapping(path = "/save")
    public String saveUser() {
        User user = new User();
        user.setAge(11L);
        user.setName("aaa");
        userService.save(user);
        return "success";
    }

    @GetMapping(path = "/get2")
    public String getUser2(@RequestParam Long id) {

        User user = (User) userService.get(id);

        return user.toString();
    }

}
