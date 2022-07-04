package com.i4rt.easyscan.lidarControl;

import java.io.IOException;
import java.math.BigInteger;
import java.net.*;
import java.util.Arrays;

public class ToHex {
    public static void main(String[] args) {
        String text = "ANGL1??dp♥A♥I??? ? ???????????????????";
        //String text1 = String.format("%040x", new BigInteger(1, text.getBytes(/*YOUR_CHARSET?*/)));
        System.out.println(Arrays.toString(text.getBytes()));
        System.out.println("Two: " + String.format("%040x", new BigInteger(1, text.getBytes(/*YOUR_CHARSET?*/))));
    }
}

//public class Receiver {
//    private DatagramSocket socket;
//    private InetAddress address;
//
//    private byte[] bufSend;
//    private byte[] bufReceiver = new byte[11];
//
//    public Receiver() throws UnknownHostException, SocketException {
//        socket = new DatagramSocket();
//        address = InetAddress.getByName("192.168.1.125");
//    }
//
//    public byte[] sendData(byte[] msg) throws IOException {
//        bufSend = msg;
//        DatagramPacket packet
//                = new DatagramPacket(bufSend, bufSend.length, address, 30020);
//        System.out.println("presending");
//        socket.send(packet);
//        System.out.println("aftersending");
//        packet = new DatagramPacket(bufReceiver, bufReceiver.length);
//        System.out.println("prereceiving");
//        socket.receive(packet);
//        System.out.println(bufReceiver);
//        System.out.println("afterreceiving");
//
//        return bufReceiver;
//    }
//
//    public void close() {
//        socket.close();
//    }
//}
