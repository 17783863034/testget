package com.atguigu.sso.service.impl.utils;


public interface Function<E,T> {
    public T callback(E e);
}