package com.i4rt.easyscan.lidarControl;

import org.python.antlr.ast.Str;

import java.io.IOException;

public class CRC16 {
    public static void main(String[] args) throws IOException, InterruptedException {
        byte[] bytes = new byte[] { 0x00, 0x00, 0x02, 0x03 };
        System.out.println((byte) 0x30);
        System.out.println((byte) 0xc0);
        String result = getCRC2(bytes);
        String print = "";
        for (byte i: bytes){
            String hex = Integer.toHexString(i);
            //print += "\\u00";
            if (hex.length() == 1) print += 0;
            print += hex;

        }
        print += result;
        //System.out.println(print);
        //!return print;
        System.out.println(print);
        //Thread.sleep(5000);
    }

    public static String getCRC2(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= (int) bytes[i];
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) == 1) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        //Exchange high low, low in front, high in back
        //CRC = ((CRC & 0x0000FF00) >> 8) | ((CRC & 0x000000FF) << 8);
        String result = Integer.toHexString(CRC);
        return result;
    }
}
