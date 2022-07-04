package com.i4rt.easyscan.lidarControl;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.python.antlr.ast.Str;

import java.util.ArrayList;
import java.util.Arrays;

public class Connect {
    private static SerialPort serialPort;

    public static SerialPort connect() {
        //Передаём в конструктор имя порта
        serialPort = new SerialPort("COM5");
        try {
            //Открываем порт
            serialPort.openPort();
            //Выставляем параметры
            serialPort.setParams(SerialPort.BAUDRATE_57600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.closePort();

        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
        return serialPort;
    }

}
