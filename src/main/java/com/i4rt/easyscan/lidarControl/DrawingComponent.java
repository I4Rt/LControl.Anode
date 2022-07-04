package com.i4rt.easyscan.lidarControl;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

class DrawingComponent extends JPanel {
    double[] xg =  CountingTest.getXg();
    double[] yg =  CountingTest.getYg();
    double[] ref = CountingTest.getRefl();

    @Override
    protected void paintComponent(Graphics gh) {

        Graphics2D drp = (Graphics2D) gh;
        //drp.drawLine(20, 340, 20, 20);
        //drp.drawLine(20, 340, 460, 340);
        //drp.drawPolyline(xg, yg, ng);

        for (int i = 0; i < xg.length; i++){
            if (ref[i] < 20) drp.setColor(Color.black);
            if (ref[i] >= 20 && ref[i] < 40) drp.setColor(Color.green);
            if (ref[i] >= 40 && ref[i] < 60) drp.setColor(Color.red);
            if (ref[i] >= 60 && ref[i] < 80) drp.setColor(Color.yellow);
            if (ref[i] >= 80 && ref[i] < 100) drp.setColor(Color.blue);
            if (ref[i] >= 100) drp.setColor(Color.blue);
            drp.drawOval(((int) (xg[i]*100)) + 150, ((int) (yg[i]*100)) + 100, 1, 1);
            //System.out.println(xg[i]);

        }
    }
}


