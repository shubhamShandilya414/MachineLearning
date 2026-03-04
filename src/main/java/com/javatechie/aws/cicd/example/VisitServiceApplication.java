package com.javatechie.aws.cicd.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/visits")
public class VisitServiceApplication {

    @Autowired
    private VisitDao visitDao;

    @GetMapping
    public List<Visit> fetchVisits(@RequestParam(required = false) String fromDate) {
        // NEW: call the repository method with the 'fromDate' parameter
        return visitDao.getVisits(fromDate);
    }

    public static void main(String[] args) {
        SpringApplication.run(VisitServiceApplication.class, args);
    }
}