package com.i4rt.easyscan.lidarControl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

public class TestPyFile {
    public static void main(String[] args) throws IOException {
        String testPython = "b''0202020200000011734541204c4d447363616e6461746120013c2022-07-07 14:21:28.7745072022-07-07 14:21:37.107703Writing complete[0.8743025619298996, 1.2486334611097103, 0.0032]157[]Range: 0.04363965140356801Min digression: 0Max digression: 0.0029592173366062096left: 2.0500000000000242, right: 2.0500000000000242Center: 2.0208000000000252real center: 2.05{'horizontal': [[1.6, -0.01, 0.003], [0, -0.02, 0], [0, 0, 0]], 'vertical': [[0, 0, 0], [0, 0, 0], [0, 0, 0]]}ok";
        testPython = testPython.substring(testPython.indexOf("{"), testPython.length()-2);
        JsonObject jsonObject = new JsonParser().parse(testPython).getAsJsonObject();
        System.out.println(getValues(jsonObject, "horizontal"));
        System.out.println(getValues(jsonObject, "vertical"));

//        System.out.println(jsonObject.getAsJsonArray("horizontal").get(0).getAsJsonArray().get(0).getAsInt());
        System.out.println(testPython);
    }

    public static ArrayList<Double> getValues(JsonObject jsonObject, String side){
        ArrayList<Double> arrayList = new ArrayList<>();
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
