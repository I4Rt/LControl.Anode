package com.i4rt.easyscan.controllers;

import com.i4rt.easyscan.interfaces.ScanResultsRepo;
import com.i4rt.easyscan.model.ScanResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private final ScanResultsRepo scanResultsRepo;

    public RestController(ScanResultsRepo scanResultsRepo) {
        this.scanResultsRepo = scanResultsRepo;
    }


    @RequestMapping(value = "startScan", method = RequestMethod.POST)
    public String update(@RequestBody String row_data) throws InterruptedException {
        ScanResults new_data = new ScanResults();
        new_data.setAcceptMark(true);
        new_data.setType(1);
        new_data.setH1(50.1);
        new_data.setH2(50.1);
        new_data.setH3(50.1);
        new_data.setL1(30.1);
        new_data.setL2(30.1);
        new_data.setL3(30.1);
        new_data.setMaxDeviation(0.01);
        new_data.setMinDeviation(-0.01);
        new_data.setTime("11.11.2001 11:01:32.11432");
        System.out.println(new_data);
        scanResultsRepo.save(new_data);

        List<ScanResults> data = scanResultsRepo.findAllOrderById();
        return new_data.getHTML();
    }


    @RequestMapping(value = "rebootLidar", method = RequestMethod.POST)
    public String reboot(@RequestBody String row_data) throws InterruptedException {
        Thread.sleep(1000);
        return "reboot_done";
    }

    @RequestMapping(value = "configureLidar", method = RequestMethod.POST)
    public String configureLidar(@RequestBody String row_data) throws InterruptedException {
        Thread.sleep(1000);
        return "configure_done";
    }

    @RequestMapping(value = "runCart", method = RequestMethod.POST)
    public String runCart(@RequestBody String row_data) throws InterruptedException {
        Thread.sleep(1000);
        return "run_cart";
    }

    @RequestMapping(value = "stopCart", method = RequestMethod.POST)
    public String stopCart(@RequestBody String row_data) throws InterruptedException {
        Thread.sleep(1000);
        return "cart_stopped";
    }
    @RequestMapping(value = "resetCart", method = RequestMethod.POST)
    public String resetCart(@RequestBody String row_data) throws InterruptedException {
        Thread.sleep(1000);
        return "cart_reset";
    }

}
