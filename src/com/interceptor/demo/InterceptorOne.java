package com.interceptor.demo;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public class InterceptorOne implements Interceptor {
    public Logger logger = Logger.getLogger(InterceptorOne.class.getName());
    @Override
    public boolean before(Object proxy, Object target, Method method,
            Object[] args) {
        logger.info("拦截之前");
        //通过返回值判断是否拦截,false为拦截
        return true;
    }

    @Override
    public void doIntercept(Object proxy, Object target, Method method,
            Object[] args) {
        logger.info("拦截");
    }

    @Override
    public void after(Object proxy, Object target, Method method,
            Object[] args) {
        logger.info("拦截之后");
    }

}
