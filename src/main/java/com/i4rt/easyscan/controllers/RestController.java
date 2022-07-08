package com.i4rt.easyscan.controllers;

import com.i4rt.easyscan.interfaces.ScanResultsRepo;
import com.i4rt.easyscan.lidarControl.ComPort;
import com.i4rt.easyscan.lidarControl.Connect;
import com.i4rt.easyscan.lidarControl.CountingTest;
import com.i4rt.easyscan.lidarControl.TestPy;
import com.i4rt.easyscan.model.ScanResults;
import jssc.SerialPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private final ScanResultsRepo scanResultsRepo;
    SerialPort serialPort = Connect.connect();


    public RestController(ScanResultsRepo scanResultsRepo) {
        this.scanResultsRepo = scanResultsRepo;
    }


    @RequestMapping(value = "startScan", method = RequestMethod.POST)
    public String startScan(@RequestBody String row_data) throws InterruptedException, IOException {
        System.out.println("Start Scan!!!");
        ArrayList<Double> arrayList = new ArrayList<>();
        ScanResults scanResults = new ScanResults();
        try {
            System.out.println("run");
            scanResults = TestPy.runPython(5);
            System.out.println(arrayList);
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
        System.out.println("run end");
        scanResultsRepo.save(scanResults);

//        ScanResults scanResults = new ScanResults(true, 1, "1", 1.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        //ComPort.connect(serialPort, 3);
//        CountingTest.run();
        Thread.sleep(1000);
//        List<ScanResults> data = scanResultsRepo.findAllOrderById();
        return scanResults.getHTML();
    }

    @RequestMapping(value = "rebootLidar", method = RequestMethod.POST)
    public String reboot(@RequestBody String row_data) throws InterruptedException {
        //try {
        //    TestPy.runPython(3);
        //} catch (IOException | InterruptedException ioException) {
        //    ioException.printStackTrace();
        //}
        Thread.sleep(1000);
        System.out.println("Reboot");
        return "reboot_done";
    }

    @RequestMapping(value = "configureLidar", method = RequestMethod.POST)
    public String configureLidar(@RequestBody String row_data) throws InterruptedException {
//        try {
//            TestPy.runPython(2);
//            TestPy.runPython(2);
//        } catch (IOException | InterruptedException ioException) {
//            ioException.printStackTrace();
//        }
        Thread.sleep(1000);
        System.out.println("Configure!!!");
        return "configure_done";
    }

    @RequestMapping(value = "runCart", method = RequestMethod.POST)
    public String runCart(@RequestBody String row_data) throws InterruptedException {
        ComPort.connect(serialPort, 3);
        Thread.sleep(1000);
        System.out.println("Start Lidar");
        return "run_cart";
    }

    @RequestMapping(value = "stopCart", method = RequestMethod.POST)
    public String stopCart(@RequestBody String row_data) throws InterruptedException {
        ComPort.connect(serialPort, 1);
        Thread.sleep(1000);
        System.out.println("Stop Lidar");
        return "cart_stopped";
    }
    @RequestMapping(value = "resetCart", method = RequestMethod.POST)
    public String resetCart(@RequestBody String row_data) throws InterruptedException {
        ComPort.connect(serialPort, 2);
        Thread.sleep(1000);
        System.out.println("Initial position");
        return "cart_reset";
    }

    @RequestMapping(value = "getExistScan/{id}", method = RequestMethod.GET)
    public String getExistScan(@PathVariable Long id) throws InterruptedException {
        return scanResultsRepo.getById(id).getHTML();
    }

    @RequestMapping(value = "getShort", method = RequestMethod.GET)
    public String getShort(){
        //return "1";
        return scanResultsRepo.findAllOrderById().get(0).getShortHTML();
    }
}
