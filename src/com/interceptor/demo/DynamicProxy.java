package com.interceptor.demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
/**
 * JDK动态代理配置拦截器
 * @author Cherry
 * 2020年5月28日
 */
public class DynamicProxy implements InvocationHandler {
    private Object target;
    private String interceptorClass;
    
    public DynamicProxy(Object target,String clazz) {
        this.target = target;
        this.interceptorClass = clazz;
    }
    
    public static Object bind(Object target,String interceptorClass) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), 
                target.getClass().getInterfaces(),new DynamicProxy(target,interceptorClass));
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Object result = null;
        if(interceptorClass == null) {
            return method.invoke(target, args);
        }
        @SuppressWarnings("deprecation")
        Interceptor inter = (Interceptor) Class.forName(interceptorClass).newInstance();
        if(inter.before(proxy, target, method, args)) {
            result = method.invoke(target, args);
        }else {
            inter.doIntercept(proxy, target, method, args);
        }
        inter.after(proxy, target, method, args);
        return result;
    }

}
