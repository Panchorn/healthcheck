package com.assignment.healthcheck.controller;

import com.assignment.healthcheck.model.Report;
import com.assignment.healthcheck.service.HealthCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/websites")
public class HealthCheckController {

    @Autowired
    private HealthCheckService healthCheckService;

    @PostMapping(value = "/validation")
    public Report check(@RequestParam("file") MultipartFile file) {
        return healthCheckService.check(file);
    }

}
