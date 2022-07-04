package com.i4rt.easyscan.lidarControl;

import java.io.IOException;

public class TestPyFile {
    public static void main(String[] args) throws IOException {
        String testPython = "C:\\Users\\boss\\AppData\\Local\\Programs\\Python\\Python39\\python.exe" +
                " D:\\GitHub\\EasyScan\\src\\main\\java\\com\\i4rt\\easyscan\\lidarControl\\laser_test.py";
        Runtime.getRuntime().exec(testPython);
        System.out.println("ok");
    }
}
