package com.i4rt.easyscan.lidarControl;

import org.python.antlr.ast.Str;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.codec.binary.Hex;

public class CountingTest {

    private static double[] xg;
    private static double[] yg;
    private static double[] refl;
    private static double[] angles;

    public static double[] getXg() {
        return xg;
    }
    public static double[] getYg() {
        return yg;
    }
    public static double[] getRefl() {
        return refl;
    }
    public static ArrayList<String> getValues() {
        return values;
    }

    static String fullText;
    static int angleCount;

    static List<Double> coordinates = new ArrayList<>();
    static ArrayList<Double> cArraysLen = new ArrayList<>();
    static ArrayList<Double> cArraysRefl = new ArrayList<>();
    static ArrayList<Double> cArraysAngle = new ArrayList<>();
    static ArrayList<String> values = new ArrayList<>();

    static double z;

    public static void counting(){
        List<Double> arrays = new ArrayList<>();
        double len;
        double angleRad;
        for (int i = 0; i < cArraysLen.size(); i++){
            len = cArraysLen.get(i);
            angleRad = cArraysAngle.get(i) * Math.PI / 180;
            double x = Math.cos(angleRad) * len;
            double y = Math.sin(angleRad) * len;
            if (len != 0){
                arrays.add(x);
                coordinates.add(x);
                arrays.add(y);
                coordinates.add(y);
                arrays.add(cArraysRefl.get(i));
                coordinates.add(cArraysRefl.get(i));
                arrays.add(cArraysAngle.get(i));
                coordinates.add(cArraysAngle.get(i));
                coordinates.add(z);
            }
        }
        //System.out.println(cArraysRefl);
        xg = new double[arrays.size()/4];
        yg = new double[arrays.size()/4];
        refl = new double[arrays.size()/4];
        angles = new double[arrays.size()/4];
        //System.out.println("xg: ");
        for (int i = 0; i < arrays.size()/4; i++){
            if (refl[i] < 20){
                xg[i] = (arrays.get(i * 4));
                //System.out.print(xg[i] + ", ");
                yg[i] = (arrays.get(i * 4 + 1));
                refl[i] = (arrays.get(i * 4 + 2));
                angles[i] = (arrays.get(i * 4 + 3));
            }
        }
    }

    public static void countingArrays(String coords, int choice){
        angleCount = 0;
        while (coords.length() > 1){
            double len = Integer.parseInt(coords.substring(0, 4), 16);
            //String len = coords.substring(0, 2);
            coords = coords.substring(4);
            if (choice == 1) cArraysLen.add(len/10000);
            else if (choice == 2) cArraysRefl.add(len);
            else if (choice == 3){
                cArraysAngle.add((55 + (angleCount * 0.0833)));
                angleCount++;
            }
        }
    }

    public static void run() throws IOException, InterruptedException {
        //TestServer.runServer();
        int a = 0;
        int b = 0;
//        fullText = TestPy.runPython(1);
        //fullText = fullText.substring(2, fullText.length()-2);
        //fullText = fullText.substring(56, fullText.length()-2);
        //System.out.println(fullText);
        String[] textArray = fullText.split("', '");
        System.out.println("first");
        //!DataFromLidar.runPython();
        //!String[] textArray = DataFromLidar.getFullText();
        //System.out.println("Array: " + Arrays.toString(textArray));
        //System.out.println(Arrays.toString(textArray));
        System.out.println(Arrays.toString(textArray));
        z = 0.0015;
        //System.out.println("before deleted coordinates");
        coordinates = new ArrayList<>();
        //System.out.println("deleted coordinates");
        System.out.println("length: " + textArray.length);
        StringBuilder hex = new StringBuilder();
        ArrayList<String> newTextArray = new ArrayList<>();
        System.out.println("second");
        for (int i = 0; i < textArray.length; i++){
            System.out.println("one: " + textArray[i]);
            char[] chars = textArray[i].toCharArray();
            for(int j = 0; j < chars.length-3; j++){
                String h = Integer.toHexString((int) chars[j]);
                if (h.equals("2") && Integer.toHexString((int) chars[j+1]).equals("2")
                        && Integer.toHexString((int) chars[j+2]).equals("2")
                        && Integer.toHexString((int) chars[j+3]).equals("2")){
                    if(!hex.toString().equals("")){
                        newTextArray.add(hex.toString());
                        //System.out.println("OK :" + hex.toString());
                    }
                    hex = new StringBuilder();
                }
                if (h.length() == 1) h = "0" + h;
                //else System.out.println("hki :" + h + h.equals("2"));
                hex.append(h);
                //assert newTextArray != null;
                //System.out.println("arraylen: " + newTextArray.size());

            }
        }
        //System.out.println("two: " + hex.toString());
        //String value = null;
        //System.out.println("size: " + newTextArray.size());
        System.out.println("third");
        for (int i = 1; i < newTextArray.size(); i++){
            String value = newTextArray.get(i);
            //System.out.println("value: " + value);
            //System.out.println("value: " + value);
            if (a % 25 == 0){
                //System.out.println(value.length());
                //System.out.println(value);
                cArraysAngle.removeAll(cArraysAngle);
                cArraysRefl.removeAll(cArraysRefl);
                cArraysLen.removeAll(cArraysLen);
                b++;
                System.out.println(b);
            }
            //System.out.println(value);
            value = value.substring(188, 10364);
            countingArrays(value.substring(0, 3364), 1);
            value = value.substring(3406);
            countingArrays(value.substring(0, 3364), 2);
            //System.out.println("refl: " + cArraysRefl);
            value = value.substring(3406);
            //System.out.println("refl: " + cArraysRefl);
            countingArrays(value.substring(0, 3364), 3);
            //System.out.println("refl: " + cArraysRefl);
            counting();
            a++;
            //System.out.println(a);
            z += 0.0015;
            //System.out.println("i: " + i);

        }
        System.out.println("Before CSV - fourth");
        CsvWork.runCSV(coordinates);
        //String data = null;
        //System.out.println("Before analyze - fifth");
        //try {
        //    data = TestPy.runPython(5);
        //} catch (IOException | InterruptedException ioException) {
        //    ioException.printStackTrace();
        //}
        ////data = data.substring(data.length()-258, data.length()-1);
        //System.out.println(data);
        //String[] dataArray = (data.substring(data.length()-258, data.length()-1)).split(", '");
        //for (String i : dataArray){
        //    String[] j = i.split(": ");
        //    values.add(j[1]);
        //    System.out.println(j[1]);
        //}
        System.out.println("end");

        //System.out.println(a);
        //TestAnalyze.runAnalyze();
    }

}

