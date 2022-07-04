package com.i4rt.easyscan.lidarControl;

import lombok.ToString;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TestJar {
    public static void main(String[] args) {
        String text = "d77efffdfffdfffdfffdfffdfffdfffdfffdfffde07e";
        String text1 = Arrays.toString(text.split("(?<=\\G.{4})"));
        text1 = text1.substring(1, text1.length()-1);
        String[] chars1 = text1.split(", ");
        int[] ints = new int[chars1.length];

        String prev;
        String next;
        int count = 0;
        int jPrevCheck;
        int jNextCheck;
        for(int j = 0; j < chars1.length; j++){
            ints[j] = Integer.parseInt(chars1[j], 16);
            System.out.println(ints[j] + " " + j);
            String h = chars1[j];
            //String h1 = chars1[j+1];
            h = lenToFour(h);

            if(h.equals("fffd")){
                prev = lenToFour(h);
                jPrevCheck = j;
                while (h.equals("fffd")){
                    j++;
                    h = chars1[j];
                    count++;
                }
                jNextCheck = j--;
                System.out.println(jPrevCheck + " " + jNextCheck);
                int jSrCheck = (jPrevCheck+jNextCheck);
                while (count != 0){
                    ints[jSrCheck/2] = (Integer.parseInt(chars1[jPrevCheck], 16)
                                + Integer.parseInt(chars1[jNextCheck], 16))/2;
                    if ((jSrCheck%2 != 0)){
                        ints[jSrCheck/2+1] = (Integer.parseInt(chars1[jPrevCheck], 16)
                                + Integer.parseInt(chars1[jNextCheck], 16))/2+1;
                    }
                    jSrCheck = (jSrCheck+jPrevCheck);
                    count--;
                }
            }
        }
    }

    public static String lenToFour(String h){
        while(h.length() != 4){
            h = "0" + h;
        }
        return h;
    }
}
