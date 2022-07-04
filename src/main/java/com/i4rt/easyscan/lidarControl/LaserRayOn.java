package com.i4rt.easyscan.lidarControl;

import org.python.util.PythonInterpreter;
import org.python.core.*;


import junit.framework.*;

public class LaserRayOn extends TestCase {
    private static String answer;
    PythonInterpreter interp = new PythonInterpreter();
    //String[] fullText;

    public void test() throws PyException {
        System.out.println();
        interp.exec("import socket");
        interp.exec("import time");

        interp.exec("client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)");
        interp.exec("client.connect(('192.168.0.3', 2112))");

        interp.exec("MESSAGE1 = b'\\x02\\x02\\x02\\x02\\x00\\x00\\x00\\x10\\x73\\x4D\\x4E\\x20\\x4C\\x4D\\x43\\x73\\x74\\x61\\x72\\x74\\x6D\\x65\\x61\\x73\\x68'");

        interp.exec("client.sendall(MESSAGE_1)");


        interp.exec("in_data = client.recv(100)");
        interp.exec("print(in_data)");

        //interp.exec("for _ in range(slices):\n in_data = client.recv(5195)\n  str0.append(in_data.hex())\n" +
        //        "time.sleep(seconds_period)");

        //interp.exec("client.sendall(MESSAGE_STOP)");
        answer = interp.get("in_data", String.class);

        //System.out.println(str0.toString());
        //PyObject a = interp.get("a");
        //System.out.println(str);
        //System.out.println(a);
    }

    public static void runPython() throws PyException {
        junit.textui.TestRunner.run(PythonInterpreterGetting.class);
    }

    public static String getFullText() {
        return answer;
    }
}
