package com.proxy.demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Logger;

/**
 * Proxy 动态代理和静态代理
 * @author Cherry
 * 2020年4月20日
 */

public class JavaProxyDemo {

    public static void main(String[] args) {
        //静态代理
        StaticProxy statics = new StaticProxy(new Monitor());
        statics.print("Hello world !");
 
        //动态代理  操作多个接口
        DynamicProxy dp = new DynamicProxy();
        Show show = (Show) dp.bind(new Monitor());//接口Show
        show.print("Hello World !");
        Show2 show2 = (Show2) dp.bind(new Monitor());//接口Show2
        show2.show();
    }

}
//接口
interface Show{
    void print(String p);
}
interface Show2{
    void show();
}
//代理类和被代理类实现同一接口
class Monitor implements Show,Show2{
    @Override
    public void print(String p) {
        System.out.println(p);
    }

    @Override
    public void show() {
        System.out.println("Show2");
    }
}
//静态代理
class StaticProxy implements Show{
    private Monitor monitor;
    public StaticProxy(Monitor m) {
        this.monitor = m;
    }
    //插入日志
    @Override
    public void print(String p) {
        log("方法之前");
        monitor.print(p);
        log("方法之后");
    }
    //日志记录
    private void log(String msg) {
        Logger.getLogger(StaticProxy.class.getName()).info(msg);
    }
}
//动态代理  实现InvocationHandler 可处理多个接口
class DynamicProxy implements InvocationHandler{
    private Object target;
    
    public Object bind(Object obj) {
        this.target = obj;
        //返回Proxy.newProxyInstance(loader, interfaces, h)
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        //代理方法
        log(method.getName()+"方法之前");
        Object result = method.invoke(target, args);
        log(method.getName()+"方法之后");
        return result;
    }
    //日志记录
    private void log(String msg) {
        Logger.getLogger(DynamicProxy.class.getName()).info(msg);
    }
}