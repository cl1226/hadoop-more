package com.cnnc.springbootmybatis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cnnc.springbootmybatis.dao.UserDao;
import com.cnnc.springbootmybatis.domain.User;
import com.cnnc.springbootmybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService<User, Long> {

    @Autowired
    private UserDao dao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int save(User user) {
        int count = dao.save(user);
        return count;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public User get(Long id) {
        JSONObject jsonObject = (JSONObject) dao.get(id);
        if (jsonObject != null) {
            User user = JSONObject.parseObject(jsonObject.toString(), User.class);
            return user;
        }
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }


}
