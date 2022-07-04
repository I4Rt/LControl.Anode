package com.i4rt.easyscan.lidarControl;

import org.python.antlr.ast.Str;
import org.python.core.AstList;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/*
 Процесс:
	 1. Создайте службу Socket и укажите IP-адрес и соответствующий номер порта подключающегося сервера.
	 2. Получите соответствующий объект OutputStream через объект Socket и отправьте данные на сервер.
	 3. Получите соответствующий объект InputStream через объект Socket и получите данные, отправленные сервером.
	 4. Отключите службу.
 */
public class TestServer {
    public static void runServer() throws IOException, InterruptedException {
        System.out.println("start");
        Socket socket = new Socket("192.168.0.3", 2112);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_16));

        //DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        System.out.println("point");
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);

        DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

        String logIn = "\u0002\u0002\u0002\u0002\u0000\u0000" +
                "\u0000\u0017\u0073\u004D\u004E\u0020\u0053\u0065\u0074\u0041\u0063\u0063\u0065\u0073\u0073" +
                "\u004D\u006F\u0064\u0065\u0020\u0003\u00F4\u0072\u0047\u0044\u00B3";
        String scanData1 = "\u0002\u0002\u0002\u0002\u0000\u0000\u0000\u0011\u0073\u0045\u004E\u0020" +
                "\u004C\u004D\u0044\u0073\u0063\u0061\u006E\u0064\u0061\u0074\u0061\u0020\u0001\u0033";

        String scanDataMon1 = "\u0002\u0002\u0002\u0002\u0000\u0000\u0000\u0014\u0073\u0045\u004E\u0020\u004C\u004D" +
                "\u0044\u0073\u0063\u0061\u006E\u0064\u0061\u0074\u0061\u006D\u006F\u006E\u0020\u0001\u005F";

        String stopScan = "\u0002\u0002\u0002\u0002\u0000\u0000\u0000\u0014\u0073\u0045\u004E\u0020\u004C\u004D" +
                "\u0044\u0073\u0063\u0061\u006E\u0064\u0061\u0074\u0061\u006D\u006F\u006E\u0020\u0000\u005E";
        dOut.writeBytes(scanDataMon1);

        // Реализуем ввод сообщений из консоли
        String line = "";
        ArrayList<String> textArray = new ArrayList<>();
        List<Integer> ints = new ArrayList<>();
        String lineArray;
        int n = 0;
        char[] chars1 = new char[0];
        System.out.println("bytes: ");

        while (n < 15){
            String readeLine = reader.readLine();
            Thread.sleep(200);
            System.out.println("readline: " + readeLine);
            line += readeLine;
            chars1 = line.toCharArray();
            n++;
        }
        //char[] chars = new char[0];

        dOut.writeBytes(stopScan);

        System.out.println("line: " + line);
        System.out.println(line.length());
        System.out.println("chars: " + Arrays.toString(chars1));
        //for (char ch: chars1){
        //    byte b = (byte)ch;
        //    ints.add(Byte.toUnsignedInt(b));
        //    System.out.print(ch);
        //}
        //System.out.println("point1");

        //System.out.println("check");

        System.out.println();
        System.out.println("line: " + line);

        textArray.add(line);

        StringBuilder hex = new StringBuilder();
        ArrayList<String> newTextArray = new ArrayList<>();
        System.out.println("size: " + textArray.size());

        for (int i = 0; i < textArray.size(); i++){

            String prev;
            String next;
            int jPrevCheck;
            int jNextCheck;
            //System.out.println("one: " + textArray.get(i));
            for(int j = 0; j < chars1.length-1; j++){
                String h = Integer.toHexString((int) chars1[j]);
                String h1 = Integer.toHexString((int) chars1[j+1]);
                h = lenToFour(h);
                h1 = lenToFour(h1);

                if(h.equals("fffd")){
                    prev = lenToFour(Integer.toHexString((int) chars1[j-1]));
                    jPrevCheck = j;
                    while (h.equals("fffd")){
                        j++;
                        h = Integer.toHexString((int) chars1[j]);

                    }
                }

                if (h.equals("0202") && h1.equals("0202") || h.equals("0202") && h1.equals("0200")){
                    System.out.println("first true");
                    if (hex.toString().length() == 10378) hex.insert(0, "02");
                    System.out.println("len1: " + hex.toString().length());
                    if(!hex.toString().equals("") && hex.toString().length() > 10000){
                        System.out.println("second true");
                        newTextArray.add(hex.toString());
                        System.out.println("len2: " + hex.toString().length());
                        //System.out.println("OK :" + hex.toString());
                    }
                    hex = new StringBuilder();
                }
                //if (h.length() == 1) h = "0" + h;
                //else System.out.println("hki :" + h + h.equals("2"));
                hex.append(h);
                //assert newTextArray != null;
                //System.out.println("arraylen: " + newTextArray.size());
                //System.out.println("two: " + hex.toString());
            }
        }
        System.out.println("OK :" + hex.toString());
        System.out.println("Hex = " + newTextArray);
        System.out.println(newTextArray.size());
        for (String s: newTextArray){
            System.out.println(s.length());
        }

        //System.out.println("ASCII = " + line);
        System.out.println("Hex = " + newTextArray.get(100));
        System.out.println(line.length());

        //for (int i = 0; i < lineArray.length; i++){
        //    System.out.println("word: " + lineArray[i]);
        //}

        socket.close();


        //System.out.println("«Старт клиента»");
        //System.out.println("------------------------");
        //// 1. Создайте службу Socket и укажите IP-адрес и соответствующий номер порта подключающегося сервера.
        //Socket socket = new Socket("192.168.0.3", 2112);
        //
        //// 2. Получение соответствующего объекта OutputStream через объект Socket и отправка данных на сервер
        //OutputStream outputStream = socket.getOutputStream();
        //
        //
        //
        ////outputStream.write(Integer.parseInt("\u0002\u0002\u0002\u0002\u0000\u0000" +
        ////        "\u0000\u0017\u0073\u004D\u004E\u0020\u0053\u0065\u0074\u0041\u0063\u0063\u0065\u0073\u0073" +
        ////        "\u004D\u006F\u0064\u0065\u0020\u0003\u00F4\u0072\u0047\u0044\u00B3".getBytes()));
        //
        //// 3. Получение соответствующего объекта InputStream через объект Socket и получение данных, отправленных сервером
        //InputStream inputStream = socket.getInputStream();
        //InputStreamReader  isr = new InputStreamReader(inputStream);
    }

    public static String lenToFour(String h){
        while(h.length() != 4){
            h = "0" + h;
        }
        return h;
    }
}

