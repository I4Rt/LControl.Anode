package com.i4rt.easyscan.lidarControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Arrays;

public class ServerTwo {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] bufSend;
    private byte[] bufReceiver = new byte[5195];

    byte[] messageStart = new byte[] {(byte) 0x02, (byte) 0x02, (byte) 0x02, (byte) 0x02,
            (byte)0x00, (byte) 0x00, (byte) 0x00, (byte) 0x14, (byte) 0x73,
            (byte)0x45, (byte) 0x4E, (byte) 0x20, (byte) 0x4C, (byte) 0x4D, (byte) 0x44, (byte) 0x73,
            (byte)0x63, (byte) 0x61, (byte) 0x6E, (byte) 0x64, (byte) 0x61, (byte) 0x74,
            (byte)0x61, (byte) 0x6D, (byte) 0x6F, (byte) 0x6E, (byte) 0x20, (byte) 0x01, (byte) 0x5F};

    public void sendData() throws IOException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("192.168.0.3");
        System.out.println(address);

        bufSend = messageStart;
        DatagramPacket packet
                = new DatagramPacket(bufSend, bufSend.length, address, 2112);
        System.out.println("presending");
        socket.send(packet);
        System.out.println("aftersending");
        packet = new DatagramPacket(bufReceiver, bufReceiver.length);
        System.out.println("prereceiving");
        socket.receive(packet);
        System.out.println(Arrays.toString(bufReceiver));
        System.out.println("afterreceiving");

    }

    public void close() {
        socket.close();
    }
}
