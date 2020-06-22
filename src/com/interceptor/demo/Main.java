package com.interceptor.demo;

public class Main {
    public static void main(String[] args) {
        Show s = (Show) DynamicProxy.bind(new Monitor(), InterceptorOne.class.getName());
        s.print("hello world!");
        
        Show2 s2 = (Show2) DynamicProxy.bind(new Monitor(), null);
        s2.show();
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