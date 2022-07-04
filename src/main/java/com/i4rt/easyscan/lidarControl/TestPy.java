package com.i4rt.easyscan.lidarControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.RuntimeMXBean;

public class TestPy {

    public static String runPython(int choice) throws IOException, InterruptedException{
        String text = "";
        //String textNew = "";
        //String[] argsRun = new String[] {"C:\\Users\\boss\\AppData\\Local\\Programs\\Python\\Python39\\python.exe",
        //        "D:\\GitHub\\Lidar\\LaserWorkMaven1\\src\\main\\java\\laser_test.py"};
        //String[] argsConf = new String[] {"C:\\Users\\boss\\AppData\\Local\\Programs\\Python\\Python39\\python.exe",
        //        "D:\\GitHub\\Lidar\\LaserWorkMaven1\\src\\main\\java\\configure.py"};
        //String[] argsReboot = new String[] {"C:\\Users\\boss\\AppData\\Local\\Programs\\Python\\Python39\\python.exe",
        //        "D:\\GitHub\\Lidar\\LaserWorkMaven1\\src\\main\\java\\reboot.py"};

        String runScanning = "C:\\Users\\boss\\AppData\\Local\\Programs\\Python\\Python39\\python.exe" +
                " D:\\GitHub\\EasyScan\\src\\main\\java\\com\\i4rt\\easyscan\\lidarControl\\laser.py";
        String configure = "C:\\Users\\boss\\AppData\\Local\\Programs\\Python\\Python39\\python.exe" +
                " D:\\GitHub\\EasyScan\\src\\main\\java\\com\\i4rt\\easyscan\\lidarControl\\configure.py";
        String reboot = "C:\\Users\\boss\\AppData\\Local\\Programs\\Python\\Python39\\python.exe" +
                " D:\\GitHub\\EasyScan\\src\\main\\java\\com\\i4rt\\easyscan\\lidarControl\\reboot.py";
        String laserRayOn = "C:\\Users\\boss\\AppData\\Local\\Programs\\Python\\Python39\\python.exe" +
                " D:\\GitHub\\EasyScan\\src\\main\\java\\com\\i4rt\\easyscan\\lidarControl\\laser_ray_on.py";
        String dataAnalysis = "C:\\Users\\boss\\AppData\\Local\\Programs\\Python\\Python39\\python.exe" +
                " D:\\GitHub\\EasyScan\\src\\main\\java\\com\\i4rt\\easyscan\\lidarControl\\data_analize.py";
        Process process = null;

        int n = 0;
        while (n < 1){
            if (choice == 1) process = Runtime.getRuntime().exec(runScanning);
            else if (choice == 2) process = Runtime.getRuntime().exec(configure);
            else if (choice == 3) process = Runtime.getRuntime().exec(reboot);
            else if (choice == 4) process = Runtime.getRuntime().exec(laserRayOn);
            else if (choice == 5) process = Runtime.getRuntime().exec(dataAnalysis);

            InputStream stdout = process.getInputStream();
            InputStream stderr = process.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stdout);
            InputStreamReader isrerr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            BufferedReader brerr = new BufferedReader(isrerr);

            String line;
            boolean success = false;
            if (choice == 5){
                while ((line = br.readLine()) != null) {
                    //line = line.substring(2, line.length()-2);
                    text += line;
                    //text += "', '";
                    success = true;
                }
            }
            else {
                while ((line = br.readLine()) != null) {
                    line = line.substring(2, line.length()-2);
                    text += line.toUpperCase();
                    text += "', '";
                    success = true;
                }
            }
            if (success) System.out.println("success");
            n++;
        }
        return text;

        //Runtime.getRuntime().exe


        //process.waitFor();

        //String [] SS = new String [7];
        //int k = 0;
        //for (int i=0;i<text.length()-1;i=i+2) {
        //    String S1 = text.substring(i, i+2);
        //    //textNew += S1 + " ";
        //    textNew += S1;
        //}
        //System.out.println(text);
        //System.out.println(textNew);

    }
}
