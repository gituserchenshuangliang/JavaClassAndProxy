package com.proxy.demo;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
/**
 * CGLIB代理，不需要接口，Enhancer不能够拦截final方法和inal类
 * @author Cherry
 * 2020年5月28日
 */
class CglibProxy implements MethodInterceptor{
    private Logger logger = Logger.getLogger(CglibProxy.class.getName());
    
    @SuppressWarnings("rawtypes") 
    public Object getProxy(Class clz) {
        //CGLIB enhance增强类对象
        Enhancer enhancer = new Enhancer();
        //设置增强类型
        enhancer.setSuperclass(clz);
        //设置代理对象为当前对象，要求实现MethodInterceptor接口
        enhancer.setCallback(this);
        //生成返回对象
        return enhancer.create();
    }
    
    @Override
    public Object intercept(Object proxy, Method m, Object[] args,
            MethodProxy mp) throws Throwable {
        logger.info("Method used before");
        Object obj = mp.invokeSuper(proxy, args);
        logger.info("Method used after");
        return obj;
    }
}
public class CglibDemo{
    public static void main(String[] args) {
        CglibProxy cp = new CglibProxy();
        Hello h = (Hello) cp.getProxy(Hello.class);
        h.say("中国好");
    }
}