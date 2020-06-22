package com.clazz.demo;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Java ClassLoader
 * @author Cherry
 * 2020年4月20日
 */
public class JavaClassLoaderDemo {
    /*
     * 类加载顺序 BootStrap Loader --> Extended Loader --> System Loader
     * 类委托顺序 System Loader --> Extended Loader --> BootStrap Loader
     * 其他加载器子类都排在System Loader之后，BootStrap Loader由C语言编写的
     * BootStrap Loader(sun.boot.class.path)
     * Extended Loader(java.ext.dirs)
     * System Loader(java.class.path)
     */
    public static void main(String[] args) throws IOException {
        outputSystemProperties();
    }
    
    public static void outputSystemProperties() throws IOException {
        Properties prop = System.getProperties();
        Path path = Paths.get("./src/com/clazz/demo","system.properties");
        PrintStream ps = new PrintStream(Files.newOutputStream(path));
        System.setOut(ps);
        prop.forEach((k,v) -> {
            ps.println(k+"="+v);
        });
    }
}
