package com.proxy.demo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.proxy.CallbackHelper;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

/**
 * Enhancer不能够拦截final方法和inal类
 * @author Cherry
 * 2020年5月28日
 */
public class EnhancerDemo {
    public static Logger logger = Logger.getLogger(EnhancerDemo.class.getName());
    public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //showEnhancer();
        showBeanGenerator();
    }
    
    //cglib提供的一个操作bean的工具BeanGenerator，使用它能够在运行时动态的创建一个bean。
    public static void showBeanGenerator() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        BeanGenerator generator = new BeanGenerator();
        //设置Bean属性
        generator.addProperty("username",String.class);
        generator.addProperty("password",String.class);
        
        //生产Bean对象
        Object bean = generator.create();
        
        Method setUserName = bean.getClass().getMethod("setUsername", String.class);
        Method setPassword = bean.getClass().getMethod("setPassword", String.class);
        
        //调用Bean方法
        setUserName.invoke(bean, "admin");
        setPassword.invoke(bean,"adminPass");
        
        //BeanMap类实现了Java Map，将一个bean对象中的所有属性转换为一个String-to-Obejct的Java Map
        BeanMap map = BeanMap.create(bean);
        logger.info(map.get("username")+"");
        logger.info(map.get("password")+"");
        
        Car car = new Car(1, "Benz", 320000.23);
        BeanMap carBean = BeanMap.create(car);
        logger.info(carBean.get("type")+"");
        logger.info(carBean.get("price")+"");
        
    }
    
    public static void showEnhancer() {
        Enhancer e = new Enhancer();
        e.setSuperclass(ShowOne.class);
        
        showFixedValue(e);
        showMethodInterceptor(e);
        showCallbackFilter(e);
        
        ShowOne so = (ShowOne) e.create();
        logger.info(so.show("hello.world!"));
        logger.info(so.showInt(123)+"");
    }
    
    //FixedValue用来对所有拦截的方法返回相同的值
    public static void showFixedValue(Enhancer e) {
      e.setCallback(new FixedValue() {
          @Override
          public Object loadObject() throws Exception {
              return "intercepting method";
          }
      });
    }
    
    //MethodInterceptor用来代理方法
    public static void showMethodInterceptor(Enhancer e) {
      e.setCallback(new MethodInterceptor() {
          @Override
          public Object intercept(Object obj, Method method, Object[] args,
                  MethodProxy proxy) throws Throwable {
              logger.info("方法执行之前");
              Object o = proxy.invokeSuper(obj, args);
              logger.info("方法执行之后");
              return o;
          }
      });
    }
    
    //CallbackFilter只想对特定的方法进行拦截，对其他的方法直接放行，不做任何操作
    public static void showCallbackFilter(Enhancer e) {
        CallbackHelper callbackHelper = new CallbackHelper(ShowOne.class, new Class[0]) {
            @Override
            protected Object getCallback(Method method) {
                //拦截非Object类并且返回类型为String的方法
                if(method.getDeclaringClass() != Object.class && method.getReturnType() == String.class){
                    return new FixedValue() {
                        @Override
                        public Object loadObject() throws Exception {
                            return "拦截返回值为String的方法";
                        }
                    };
                }else{
                    return NoOp.INSTANCE;
                }
            }
        };
        e.setCallbackFilter(callbackHelper);
        e.setCallbacks(callbackHelper.getCallbacks());
    }
}
class ShowOne{
    public String show(String s) {
        return s;
    }
    public int showInt(int i) {
        return i;
    }
}
class Car{
    private int no;
    private String type;
    private double price;
    public int getNo() {
        return no;
    }
    public void setNo(int no) {
        this.no = no;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public Car(int no, String type, double price) {
        super();
        this.no = no;
        this.type = type;
        this.price = price;
    }
}