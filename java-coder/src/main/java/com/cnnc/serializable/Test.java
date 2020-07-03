package com.cnnc.serializable;

import java.io.*;

public class Test {

    public static void serialize(User user) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(new File("E:\\11.txt")));
        outputStream.writeObject(user);
        outputStream.close();
    }

    public static void deserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File("E:\\11.txt")));
        User u = (User) inputStream.readObject();
        System.out.println(u);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        User user = new User();
//
//        user.setId(1);
//        user.setName("zhangsan");
//        user.setAge(10L);

//        serialize(user);
        deserialize();
    }
}
