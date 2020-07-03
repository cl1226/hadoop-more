//package com.cnnc;
//
//import com.alibaba.fastjson.JSONObject;
//import com.cnnc.dao.ICRUDDao;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@RestController
//public class AppController {
//
//    @RequestMapping(path = "/select")
//    public String testMybatisSelect() throws IOException {
//        ICRUDDao mapper = sqlSession.getMapper(ICRUDDao.class);
//        JSONObject object = (JSONObject) mapper.get(4);
//
//        return object.toJSONString();
//    }
//
//}
