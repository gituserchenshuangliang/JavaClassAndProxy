package com.clazz.demo;

public class Person {
    public String name;
    public Integer size;
    public Character sex;
    public Person(String name, Integer size, Character sex) {
        super();
        this.name = name;
        this.size = size;
        this.sex = sex;
    }
    public Person() {
        super();
    }
    
    public Person(Character sex) {
        super();
        this.sex = sex;
    }
    public Character getSex() {
        return sex;
    }
    public void show() {
        System.out.println("无参数方法");
    }
    public String showStr(String s) {
        return s;
    }
    @Override
    public String toString() {
        return "Demo [name=" + name + ", size=" + size + ", sex=" + sex + "]";
    }
}
