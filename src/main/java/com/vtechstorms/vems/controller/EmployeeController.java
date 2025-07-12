package com.vtechstorms.vems.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importEmployeeJob;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        File temp = File.createTempFile("employee", ".xlsx");
        file.transferTo(temp);

        JobParameters params = new JobParametersBuilder()
                .addString("file", temp.getAbsolutePath())
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(importEmployeeJob, params);
        return ResponseEntity.ok("Batch job started with Hibernate batching.");
    }
}
