package com.cnnc.springbootmybatis.service;

public interface ICRUDService<T, K> {

    int save(T t);

    T get(K k);

    void update();

    void delete();

}
