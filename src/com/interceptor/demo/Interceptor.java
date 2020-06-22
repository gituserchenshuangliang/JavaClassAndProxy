package com.interceptor.demo;

import java.lang.reflect.Method;

/**
 * 自定义拦截器接口
 * @author Cherry
 * 2020年5月28日
 */
public interface Interceptor {
    boolean before(Object proxy,Object target,Method method,Object[] args);
    void doIntercept(Object proxy,Object target,Method method,Object[] args);
    void after(Object proxy,Object target,Method method,Object[] args);
}
