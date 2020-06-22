package com.clazz.demo;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Reflact——反射
 * @author Cherry
 * 2020年4月19日
 */
public class JavaClazzDemo {
    @SuppressWarnings("rawtypes")
    public static Class clz = null;
    public static StringBuilder sb = new StringBuilder();
	public static void main(String[] args) throws IOException {
	    try {
	        //加载Class
           clz = Class.forName("com.clazz.demo.Person");
           //clz = Person.class;
           
           //利用反射生成Java文件
           produceJavaFile();
           
           //获取实例演示
           getInstanceShow();
           
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
	}
	
	//生成Java文件
	public static void produceJavaFile() throws IOException {
        getPackage();
        getClazz();
        getField();
        getConstructor();
        getMethds();
         
        //输出到文件
        String name = clz.getName();
        Path path = Paths.get(name.substring(name.lastIndexOf(".")+1)+".java");
        PrintStream ps = new PrintStream(Files.newOutputStream(path));
        System.setOut(ps);
        ps.print(sb.toString());
	}
	
	//类包信息
	public static void getPackage() {
	    Package pack = clz.getPackage();
	    sb.append("package ");
	    sb.append(pack.getName());
	    sb.append(";");
	    sb.append("\n");
	}
	
	//获取类信息
	public static void getClazz() {
	    int m = clz.getModifiers();
	    sb.append(Modifier.toString(m));
	    sb.append("\t");
	    sb.append(Modifier.isInterface(m)? "interface" : "class");
	    sb.append("\t");
	    String name = clz.getName();
	    sb.append(name.substring(name.lastIndexOf(".")+1));
	    sb.append("\t{\n");
	}
	
	//获取数据成员
	public static void getField() {
	    //getField()只能获取public成员
	    Field[] field = clz.getDeclaredFields();
	    for (Field f : field) {
	        int m = f.getModifiers();
	        sb.append(Modifier.toString(m));
	        sb.append("\t");
	        sb.append(Modifier.isStatic(m)? "static\t" : "");
	        sb.append(Modifier.isFinal(m)? "final\t" : "");
	        String nameType = f.getType().getName();
            sb.append(nameType.substring(nameType.lastIndexOf(".")+1));
            sb.append("\t");
	        sb.append(f.getName());
	        sb.append(";\n");
        }
	}
	
	//获取构造函数
	@SuppressWarnings("rawtypes")
    public static void getConstructor() {
	    //存放this.XXX = XXX;
	    StringBuilder sb2 = new StringBuilder();
	    
	    Constructor[] constructor = clz.getDeclaredConstructors();
	    for (Constructor c : constructor) {
	        int m = c.getModifiers();
	        sb.append(Modifier.toString(m));
	        sb.append("\t");
	        String name = c.getName();
	        sb.append(name.substring(name.lastIndexOf(".")+1));
	        sb.append("\t(");
            Parameter[] param =  c.getParameters();
            int j = param.length;
            for (int i = 0 ; i < j ; i++) {
                if(i != 0) {
                    sb.append(",\t");
                }else {
                    sb2.append("//自行补充\n");
                }
                //添加有参构造参数 
                String nameType = param[i].getType().getName();
                sb.append(nameType.substring(nameType.lastIndexOf(".")+1));
                sb.append("\t");
                sb.append(param[i].getName());
                
                //this.XXX = XXX;
                sb2.append("//this.");
                sb2.append(param[i].getName());
                sb2.append("\t=\t");
                sb2.append(param[i].getName());
                sb2.append(";\n");
            }
            sb.append(")\t{\n");
            sb.append(sb2.toString());
            sb.append("}\n");
            sb2.setLength(0);
        }
	}
	
	//声明的方法
	public static void getMethds() {
	    //getMethod()只能获取......
	    Method[] method = clz.getDeclaredMethods();
	    for (Method m : method) {
            int f = m.getModifiers();
            sb.append(Modifier.toString(f));
            sb.append("\t");
            sb.append(Modifier.isStatic(f)? "static\t" : "");
            sb.append(Modifier.isFinal(f)? "final\t" : "");
            String returnType = m.getReturnType().getName();
            sb.append(returnType.substring(returnType.lastIndexOf(".")+1));
            sb.append("\t");
            sb.append(m.getName());
            sb.append("\t{\n");
            sb.append("//具体内容不详\n");
            if(m.getReturnType().getName() != "void") {
                sb.append("return null;\n}\n");
            }else {
                sb.append("\n}\n");
            }
        }
	}
	
	//创建class实例
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public static void getInstanceShow() {
	    try {
            Class cls = Class.forName("com.clazz.demo.Demo");
            //通过构造函数获取实例,被代理对象数据成员或参数必须为类类型
            Constructor constructor = cls.getConstructor(String.class,Integer.class,Character.class);
            Object obj = constructor.newInstance("Chen",30,'M');
            //方法代理
            Method showStr = cls.getMethod("showStr", String.class);
            String str = (String) showStr.invoke(obj, "Cherry");
            
            Method getSex = cls.getMethod("getSex");
            Character sex = (Character) getSex.invoke(obj);
            
            Method show = cls.getMethod("show");
            show.invoke(obj);
            
            System.out.println(str+"\n"+sex);
        } catch (ClassNotFoundException |NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        } 
	}
}
