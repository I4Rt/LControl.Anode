package com.i4rt.easyscan.lidarControl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.i4rt.easyscan.model.ScanResults;
import org.python.antlr.ast.Str;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TestPy {

    public static ScanResults runPython(int choice) throws IOException, InterruptedException{
        String text = "";
        //String textNew = "";
        //String[] argsRun = new String[] {"D:\\Lidar\\Python38-32\\python.exe",
        //        "D:\\GitHub\\Lidar\\LaserWorkMaven1\\src\\main\\java\\laser_test.py"};
        //String[] argsConf = new String[] {"D:\\Lidar\\Python38-32\\python.exe",
        //        "D:\\GitHub\\Lidar\\LaserWorkMaven1\\src\\main\\java\\configure.py"};
        //String[] argsReboot = new String[] {"D:\\Lidar\\Python38-32\\python.exe",
        //        "D:\\GitHub\\Lidar\\LaserWorkMaven1\\src\\main\\java\\reboot.py"};

        String runScanning = "D:\\Lidar\\Python38-32\\python.exe";
//                " D:\\Lidar\\EasyScan\\src\\main\\java\\com\\i4rt\\easyscan\\lidarControl\\laser.py";
        String configure = "D:\\Lidar\\Python38-32\\python.exe";
//                " D:\\Lidar\\EasyScan\\src\\main\\java\\com\\i4rt\\easyscan\\lidarControl\\configure.py";
        String reboot = "D:\\Lidar\\Python38-32\\python.exe";
//                " D:\\Lidar\\EasyScan\\src\\main\\java\\com\\i4rt\\easyscan\\lidarControl\\reboot.py";
        String laserRayOn = "D:\\Lidar\\Python38-32\\python.exe";
//                " D:\\Lidar\\EasyScan\\src\\main\\java\\com\\i4rt\\easyscan\\lidarControl\\laser_ray_on.py";
        String dataAnalysis = "C:\\Users\\Администратор\\AppData\\Local\\Programs\\Python\\Python38-32\\python.exe" +
                " D:\\Lidar\\LControl.Anode-main\\LControl.Anode-main\\scanLidar.py";

        ProcessBuilder pb = new ProcessBuilder("C:\\Users\\Администратор\\AppData\\Local\\Programs\\Python\\Python38-32\\python.exe", "D:\\Lidar\\LControl.Anode-main\\LControl.Anode-main\\scanLidar.py");
        pb.redirectErrorStream(true);
        Process process = pb.start();

        int n = 0;
        while (n < 1){
            if (choice == 1) process = Runtime.getRuntime().exec(runScanning);
            else if (choice == 2) process = Runtime.getRuntime().exec(configure);
            else if (choice == 3) process = Runtime.getRuntime().exec(reboot);
            else if (choice == 4) process = Runtime.getRuntime().exec(laserRayOn);
            //else if (choice == 5) process = Runtime.getRuntime().exec(dataAnalysis);
            if (process != null) {
                System.out.println("not null");
//                process.waitFor();
            }

            System.out.println("choice = " + choice);

            InputStream stdout = process.getInputStream();
            InputStream stderr = process.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stdout);
            InputStreamReader isrerr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            BufferedReader brerr = new BufferedReader(isrerr);

            String line;
            boolean success = false;
            System.out.println("check1");
            if (choice == 5){
                while ((line = br.readLine()) != null) {
                    System.out.println("check 5");
                    //line = line.substring(2, line.length()-2);
                    text += line;
                    //text += "', '";
                    success = true;
                }
            }
            else {
                while ((line = br.readLine()) != null) {
                    System.out.println("another");
                    line = line.substring(2, line.length()-2);
                    text += line.toUpperCase();
                    text += "', '";
                    success = true;
                }
            }
            if (success) System.out.println("success");
            n++;
        }
        process.waitFor();
        System.out.println("ok");
//        ArrayList<Double> arrayList = new ArrayList<>();
        text = text.substring(text.indexOf("{"), text.length()-2);
        System.out.println(text);

        //System.out.println(text);
        return getScan(text);

        //Runtime.getRuntime().exe




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

    public static ScanResults getScan(String string){
        ScanResults sr = new ScanResults();
        JsonObject jsonObject = new JsonParser().parse(string).getAsJsonObject();

        sr.setL1(jsonObject.getAsJsonArray("horizontal").get(0).getAsJsonArray().get(0).getAsDouble());
        sr.setL2(jsonObject.getAsJsonArray("horizontal").get(1).getAsJsonArray().get(0).getAsDouble());
        sr.setL3(jsonObject.getAsJsonArray("horizontal").get(2).getAsJsonArray().get(0).getAsDouble());

        sr.setH1(jsonObject.getAsJsonArray("vertical").get(0).getAsJsonArray().get(0).getAsDouble());
        sr.setH2(jsonObject.getAsJsonArray("vertical").get(1).getAsJsonArray().get(0).getAsDouble());
        sr.setH3(jsonObject.getAsJsonArray("vertical").get(2).getAsJsonArray().get(0).getAsDouble());

        ArrayList<Double> positiveDeviations = new ArrayList<>();
        ArrayList<Double> negativeDeviations = new ArrayList<>();

        positiveDeviations.add(jsonObject.getAsJsonArray("horizontal").get(0).getAsJsonArray().get(1).getAsDouble());
        positiveDeviations.add(jsonObject.getAsJsonArray("horizontal").get(1).getAsJsonArray().get(1).getAsDouble());
        positiveDeviations.add(jsonObject.getAsJsonArray("horizontal").get(2).getAsJsonArray().get(1).getAsDouble());

        positiveDeviations.add(jsonObject.getAsJsonArray("vertical").get(0).getAsJsonArray().get(1).getAsDouble());
        positiveDeviations.add(jsonObject.getAsJsonArray("vertical").get(1).getAsJsonArray().get(1).getAsDouble());
        positiveDeviations.add(jsonObject.getAsJsonArray("vertical").get(2).getAsJsonArray().get(1).getAsDouble());


        negativeDeviations.add(jsonObject.getAsJsonArray("horizontal").get(0).getAsJsonArray().get(2).getAsDouble());
        negativeDeviations.add(jsonObject.getAsJsonArray("horizontal").get(1).getAsJsonArray().get(2).getAsDouble());
        negativeDeviations.add(jsonObject.getAsJsonArray("horizontal").get(2).getAsJsonArray().get(2).getAsDouble());

        negativeDeviations.add(jsonObject.getAsJsonArray("vertical").get(0).getAsJsonArray().get(2).getAsDouble());
        negativeDeviations.add(jsonObject.getAsJsonArray("vertical").get(1).getAsJsonArray().get(2).getAsDouble());
        negativeDeviations.add(jsonObject.getAsJsonArray("vertical").get(2).getAsJsonArray().get(2).getAsDouble());

        Collections.sort(positiveDeviations);
        Collections.sort(negativeDeviations);

        sr.setMaxDeviation(positiveDeviations.get(positiveDeviations.size() - 1));
        sr.setMinDeviation(-1 * negativeDeviations.get(negativeDeviations.size() - 1));

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        sr.setTime(formatter.format(date));

        Boolean needCheckMark = false;
        Boolean outOfBoundsHeight = false;
        Boolean outOfBoundsLength = false;

        final Double type1StandardLength = 1.45;
        final Double type2StandardLength = 1.55;
        final Double standardHeight = 0.575;

        final Double maxHeightDeviation = 0.015;
        final Double maxLengthDeviation = 0.02;



        if(Math.abs(sr.getL1() - sr.getL2()) < 0.04 && Math.abs(sr.getL1() - sr.getL3()) < 0.04 && Math.abs(sr.getL3() - sr.getL2()) < 0.04){
            if(Math.abs(sr.getH1() - sr.getH2()) < 0.03 && Math.abs(sr.getH1() - sr.getH3()) < 0.03 && Math.abs(sr.getH3() - sr.getH2()) < 0.03) {
                needCheckMark = false;
            }
        }
        if(sr.getL1() < (type1StandardLength + type2StandardLength)/2 && sr.getL2() < (type1StandardLength + type2StandardLength)/2 && sr.getL3() < (type1StandardLength + type2StandardLength)/2){
            sr.setType(1);
        }
        else{
            if(sr.getL1() >= (type1StandardLength + type2StandardLength)/2 && sr.getL2() >= (type1StandardLength + type2StandardLength)/2 && sr.getL3() >= (type1StandardLength + type2StandardLength)/2){
                sr.setType(2);
            }
            else{
                sr.setType(-1);
                needCheckMark = true;
            }
        }
        if(!needCheckMark){
            if((standardHeight - maxHeightDeviation < sr.getH1() && sr.getH1() < standardHeight + maxHeightDeviation) &&
                    (standardHeight - maxHeightDeviation < sr.getH2() && sr.getH2() < standardHeight + maxHeightDeviation) &&
                    (standardHeight - maxHeightDeviation < sr.getH3() && sr.getH3() < standardHeight + maxHeightDeviation)){
                outOfBoundsHeight = false;
            }
            else{
                outOfBoundsHeight = true;
            }
            Double currentStandardLength;
            if(sr.getType() == 1){
                currentStandardLength = type1StandardLength;
            }
            else {
                currentStandardLength = type2StandardLength;
            }

            if((currentStandardLength - maxLengthDeviation < sr.getL1() && sr.getL1() < currentStandardLength + maxLengthDeviation) &&
                    (currentStandardLength - maxLengthDeviation < sr.getL2() && sr.getL2() < currentStandardLength + maxLengthDeviation) &&
                    (currentStandardLength - maxLengthDeviation < sr.getL3() && sr.getL3() < currentStandardLength + maxLengthDeviation)){
                outOfBoundsLength  = false;
            }
            else{
                outOfBoundsLength  = true;
            }

            if(outOfBoundsLength || outOfBoundsHeight){
                sr.setAcceptMark(false);
            }
            else {
                sr.setAcceptMark(true);
            }
        }
        else {
            sr.setAcceptMark(false);
        }
        System.out.println(sr);
        return sr;
    }

    public static ArrayList<Double> getValues(JsonObject jsonObject, String side, ArrayList<Double> arrayList){
        double min = jsonObject.getAsJsonArray(side).get(0).getAsJsonArray().get(1).getAsDouble();
        double max = jsonObject.getAsJsonArray(side).get(0).getAsJsonArray().get(2).getAsDouble();
        for (int i = 0; i < jsonObject.getAsJsonArray(side).size(); i++){
            for (int j = 0; j < jsonObject.getAsJsonArray(side).get(0).getAsJsonArray().size(); j++){
                double param = jsonObject.getAsJsonArray(side).get(i).getAsJsonArray().get(j).getAsDouble();
                if (j == 1 && param < min) min = param;
                if (j == 2 && param > max) max = param;
                if (j == 0){
                    System.out.println("param " + param);
                    arrayList.add(param);
                }
            }
        }
        System.out.println(min);
        System.out.println(max);
        arrayList.add(min);
        arrayList.add(max);
        return arrayList;
    }
}
