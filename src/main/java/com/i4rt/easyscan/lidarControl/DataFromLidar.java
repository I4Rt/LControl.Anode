package com.i4rt.easyscan.lidarControl;

import org.python.util.PythonInterpreter;
import org.python.core.*;


import junit.framework.*;

public class DataFromLidar extends TestCase {
    private static String[] fullText;
    PythonInterpreter interp = new PythonInterpreter();
    //String[] fullText;

    public void test() throws PyException {
        System.out.println();
        //interp.exec("sys.path.append('D:\\Lidar\\Python38-32\\Lib')");


        //interp.exec("from select import cpython_compatible_select as select");
        //interp.exec("python.import.site=false");
        interp.exec("import socket");
        interp.exec("import time");


        interp.exec("slices = 5");
        interp.exec("seconds_period = 0.017");
        interp.exec("str0 = []");
        interp.exec("str1 = []");

        interp.exec("client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)");
        interp.exec("client.connect(('192.168.0.3', 2112))");

        interp.exec("MESSAGE1 = b'\\x02\\x02\\x02\\x02\\x00\\x00\\x00\\x17\\x73\\x4D\\x4E\\x20\\x53\\x65\\x74\\x41\\x63\\x63\\x65\\x73\\x73\\x4D\\x6F\\x64\\x65\\x20\\x03\\xF4\\x72\\x47\\x44\\xB3'");
        interp.exec("MESSAGE_START = b'\\x02\\x02\\x02\\x02\\x00\\x00\\x00\\x14\\x73\\x45\\x4E\\x20\\x4C\\x4D\\x44\\x73\\x63\\x61\\x6E\\x64\\x61\\x74\\x61\\x6D\\x6F\\x6E\\x20\\x01\\x5F'");
        interp.exec("MESSAGE_STOP = b'\\x02\\x02\\x02\\x02\\x00\\x00\\x00\\x14\\x73\\x45\\x4E\\x20\\x4C\\x4D\\x44\\x73\\x63\\x61\\x6E\\x64\\x61\\x74\\x61\\x6D\\x6F\\x6E\\x20\\x00\\x5E'");

        interp.exec("client.sendall(MESSAGE_START)");
        interp.exec("in_data = client.recv(29)");
        interp.exec("print(in_data)");

        interp.exec("for _ in range(slices):\n  in_data = client.recv(5195)\n  str0.append(in_data)\n" +
                "time.sleep(seconds_period)");

        interp.exec("client.sendall(MESSAGE_STOP)");
        //interp.exec("print(str0)");

        //interp.exec("for i in str0:\n  print(i)\n  str1.append(i.hex())");
        fullText = interp.get("str0", String[].class);

        //System.out.println(str0.toString());
        //PyObject a = interp.get("a");
        //System.out.println(str);
        //System.out.println(a);
    }

    public static void runPython() throws PyException {
        junit.textui.TestRunner.run(DataFromLidar.class);
    }

    public static String[] getFullText() {
        return fullText;
    }
}
