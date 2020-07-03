package com.cnnc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cnnc.dao.ICRUDDao;
import com.cnnc.domain.User;
import com.cnnc.service.ICRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements ICRUDService<User, Long> {

    @Autowired
    private ICRUDDao dao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int save(User user) {
        int count = dao.save(user);
        return count;
    }

    @Override
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
