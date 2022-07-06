package com.i4rt.easyscan.controllers;


import com.i4rt.easyscan.interfaces.ScanResultsRepo;
import com.i4rt.easyscan.model.ScanResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;


@Controller
public class MainController {

    @Autowired
    private final ScanResultsRepo scanResultsRepo;
    ScanResults scanResults = new ScanResults();

    public MainController(ScanResultsRepo scanResultsRepo) {
        this.scanResultsRepo = scanResultsRepo;
    }

    @RequestMapping(value = "/lidar", method = RequestMethod.GET)
    public String getAllScans(Model model){
        List<ScanResults> data = scanResultsRepo.findAll();

        try {
            model.addAttribute("anodes", data);
            model.addAttribute("html", scanResults.getHTML());
        }
        catch (Exception e){
            System.out.println(e);
        }

        return "main";
    }

    /*
    @GetMapping("/register")
    public String registrationRedirect(){
        return "/register";
    }

    @PostMapping("/register")
    public String addUser( User user, Model model){
        User userFromDB = userRepo.findByUsername(user.getUsername());
        if(userFromDB != null){
            model.addAttribute("result", "Пользователь с таким логином уже зарегистрирован, выберете, пожалуйста, другой.");
            return "register";
        }
        else{
            user.setRole("USER");
            user.setActive(true);
            userRepo.save(user);
        }
        return "redirect:/login";
    }
    */

}
