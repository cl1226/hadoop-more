package com.cnnc.springboot_web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
public class JdbcController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("select")
    public List<Map<String, Object>> selectAll() {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from t_user");
        return maps;
    }

    @GetMapping("insert")
    public String insertOne() {
        jdbcTemplate.execute("insert into t_user values(6, 'aaa', 12)");
        return "success";
    }

}
