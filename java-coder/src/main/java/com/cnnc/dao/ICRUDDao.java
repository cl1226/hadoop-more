package com.cnnc.dao;

public interface ICRUDDao<T, K> {

    int save(T t);

    T get(K k);

    void update();

    void delete();

}
