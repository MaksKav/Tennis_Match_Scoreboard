package com.maxkavun.dao;

public interface SavableDao <T>{
    void save(T entity);
}
