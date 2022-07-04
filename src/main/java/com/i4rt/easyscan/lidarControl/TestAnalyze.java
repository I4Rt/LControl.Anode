package com.i4rt.easyscan.lidarControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestAnalyze {
    //public static void main(String[] args) throws IOException, InterruptedException {
    //    try {
    //        runPython();
    //    } catch (IOException | InterruptedException ioException) {
    //        ioException.printStackTrace();
    //    }
    //}

    public static void runAnalyze() throws IOException, InterruptedException{
        String text = "";
        String textNew = "";
        Process p = Runtime.getRuntime().exec("C:\\Users\\boss\\AppData\\Local\\Programs\\Python\\Python39\\python.exe" +
                " D:\\LaserWorkMaven\\src\\main\\java\\Easy_v2.py");
        InputStream stdout = p.getInputStream();
        InputStream stderr = p.getErrorStream();
        InputStreamReader isr = new InputStreamReader(stdout);
        InputStreamReader isrerr = new InputStreamReader(stderr);
        BufferedReader br = new BufferedReader(isr);
        BufferedReader brerr = new BufferedReader(isrerr);

        String line;
        boolean success = false;
        //System.out.println("OUTPUT:");
        while ((line = br.readLine()) != null) {
            text = line.toUpperCase();
            System.out.println(text);
            //System.out.println(text.length());
            success = true;
        }
        //if (success) System.out.println("success");
        p.waitFor();

        //String [] SS = new String [7];
        //int k = 0;
        //for (int i=0;i<text.length()-1;i=i+2) {
        //    String S1 = text.substring(i, i+2);
        //    textNew += S1 + " ";
        //}
        //return textNew;
    }
}
