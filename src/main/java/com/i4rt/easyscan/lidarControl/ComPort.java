package com.i4rt.easyscan.lidarControl;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.python.antlr.ast.Str;

import java.util.ArrayList;
import java.util.Arrays;

public class ComPort {
    private static SerialPort serialPort;
    public static String mes;
    public static byte[] message;

    public static void connect(SerialPort s, int choice) {
        mes = "";
        serialPort = s;
        //SerialPort serialPort = s;
        //Передаём в конструктор имя порта
        if (choice == 1) message = new byte[] { 0x00, 0x00, 0x02, 0x01, (byte) 0x84, (byte) 0xc0};
        else if (choice == 2) message = new byte[] { 0x00, 0x00, 0x02, 0x02, (byte) 0x85, (byte) 0x80};
        else if (choice == 3) message = new byte[] { 0x00, 0x00, 0x02, 0x03, (byte) 0x45, (byte) 0x41};

        for (byte i: message){
            System.out.println(i);
        }

        //serialPort = new SerialPort("COM5");
        try {
            //Открываем порт
            //System.out.println(-4 == (byte) 0xFC);
            serialPort.openPort();
            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
            System.out.println("before write");
            //byte[] from_port = serialPort.readBytes();
            serialPort.writeBytes(message);
            //System.out.println("Tut: " + Arrays.toString(from_port));
            Thread.sleep(1000);

            System.out.println("Answer: " + mes);
            System.out.println("after write");
            serialPort.closePort();
        }
        catch (SerialPortException | InterruptedException ex) {

            System.out.println(ex);
        }
    }

    private static class PortReader implements SerialPortEventListener {
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0){
                try {
                    //Получаем ответ от устройства, обрабатываем данные и т.д.
                    //System.out.println("before reading");
                    byte[] data = serialPort.readBytes();
                    //!!!event.getEventValue()!!!
                    for (byte i: data){
                        mes += i + " ";
                    }

                    //System.out.println(Arrays.toString(data));
                    //if (!Arrays.toString(data).equals("[0]"))

                    //И снова отправляем запрос
                    //serialPort.writeString("Get data");
                }
                catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        }
    }
}
