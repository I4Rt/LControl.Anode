package com.i4rt.easyscan.lidarControl;

import jssc.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class Main extends JFrame implements ActionListener {
    JPanel jcp = new JPanel(new BorderLayout());
    JToolBar jtb = new JToolBar("ToolBar");
    JToolBar lidarControl = new JToolBar("Lidar control", 1);

    JPanel graph = null;
    JButton bt;
    SerialPort serialPort = Connect.connect();

    public Main() {
        super("Graph");
        setContentPane(jcp);
        super.add(jtb, BorderLayout.NORTH);
        super.add(lidarControl, BorderLayout.EAST);

        bt = new JButton("Configure");
        bt.addActionListener( this);
        //bt.setEnabled(false);
        jtb.add(bt);
        bt = new JButton("Start scan");
        bt.addActionListener(this);
        jtb.add(bt);
        bt = new JButton("Reboot");
        bt.addActionListener(this);
        //bt.setEnabled(false);
        jtb.add(bt);
        bt = new JButton("Connect to COM port");
        bt.addActionListener(this);
        //jtb.add(bt);
        bt = new JButton("Stop");
        bt.addActionListener(this);
        lidarControl.add(bt, BorderLayout.NORTH);
        bt = new JButton("Initial");
        bt.addActionListener(this);
        lidarControl.add(bt);
        bt = new JButton("Start");
        bt.addActionListener(this);
        lidarControl.add(bt);
        bt = new JButton("Laser ray on");
        bt.addActionListener(this);
        //jtb.add(bt);


        jcp.setBackground(Color.lightGray);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void startScanTrue() throws IOException, InterruptedException {
        try{
            jcp.remove(graph);
            System.out.println("deleted success");
        } catch (NullPointerException ignored){
            System.out.println("deleted error");
        }
        CountingTest.run();
        graph = new DrawingComponent();
        jcp.add(graph, BorderLayout.CENTER);
        graph.setVisible(true);
        revalidate();
        repaint();
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("Configure")){
            try {
                TestPy.runPython(2);
            } catch (IOException | InterruptedException ioException) {
                ioException.printStackTrace();
            }
            System.out.println("Configure");
        }
        if (e.getActionCommand().equals("Start scan")){
            try {
                startScanTrue();
            } catch (IOException | InterruptedException ioException) {
                ioException.printStackTrace();
            }
            System.out.println("Start scan");
        }

        if (e.getActionCommand().equals("Reboot")){
            try {
                TestPy.runPython(3);
            } catch (IOException | InterruptedException ioException) {
                ioException.printStackTrace();
            }
            System.out.println("Reboot");
        }

//        if (e.getActionCommand().equals("Connect to COM port")){
//            String message = null;
//            try {
//                message = CRC16.run();
//                //System.out.println("message: " + message);
//                //ComPort.connect(serialPort);
//
//            } catch (IOException | InterruptedException ioException) {
//                ioException.printStackTrace();
//            }
//            System.out.println("Connect to COM port");
//        }
        if (e.getActionCommand().equals("Laser ray on")){
            LaserRayOn.runPython();
            String answer = LaserRayOn.getFullText();
            System.out.println(answer);
            System.out.println("Laser ray on");
        }
        if (e.getActionCommand().equals("Stop")){
            ComPort.connect(serialPort, 1);
            System.out.println("Stop Lidar");
        }
        if (e.getActionCommand().equals("Initial")){
            ComPort.connect(serialPort, 2);
            System.out.println("Initial position");
        }
        if (e.getActionCommand().equals("Start")){
            ComPort.connect(serialPort, 3);
            System.out.println("Start Lidar");
        }
    }
    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}
