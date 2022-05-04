package com.elmenture.core.controller;

import com.elmenture.core.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by otikev on 13-Mar-2022
 */

@Controller
public class IndexController {

    @Autowired
    EmailService emailService;

    @RequestMapping("/")
    public String home(){
        return "index";
    }

}
