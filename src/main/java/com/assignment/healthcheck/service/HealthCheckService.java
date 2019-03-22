package com.assignment.healthcheck.service;

import com.assignment.healthcheck.Utility.ReadFile;
import com.assignment.healthcheck.client.RestClient;
import com.assignment.healthcheck.model.Report;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class HealthCheckService {

    @Value("${app.config.health-check-report.endpoint}")
    private String healthCheckReportEndpoint;

    @Autowired
    private RestClient restClient;

    public Report check(MultipartFile file) {
        System.out.println("\nPerform website checking...");
        List<String> csv = ReadFile.convertMultipartFileToList(file);
        Report report = new Report();

        Long start = System.nanoTime();
        List<Boolean> resultBoolean = callApiByList(csv);
        resultBoolean
                .stream()
                .forEach(aBoolean -> {
                    report.increaseTotalWebsites();
                    if (aBoolean) {
                        report.increaseSuccess();
                    } else {
                        report.increaseFailure();
                    }
                });
        report.setTotal_time(System.nanoTime() - start);

        System.out.println("Done!\n");
        System.out.println("Checked webistes: " + report.getTotal_websites());
        System.out.println("Successful websites: " + report.getSuccess());
        System.out.println("Failure websites: " + report.getFailure());
        System.out.println("Total times to finished checking website: " +
                TimeUnit.SECONDS.convert(report.getTotal_time(), TimeUnit.NANOSECONDS) +
                " second");

        String sendResponse = sendReport(report) ? "success" : "fail";
        System.out.println("\nsend report " + sendResponse);

        return report;
    }

    private List<Boolean> callApiByList(List<String> apiList) {
        return apiList
                .stream()
                .map(s -> CompletableFuture.supplyAsync(() -> callHealthCheckAsync(s)))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    @Async
    public Boolean callHealthCheckAsync(String endpoint) {
        Try<ResponseEntity> _try = restClient.get(endpoint);
        if (_try.isSuccess() && !HttpStatus.REQUEST_TIMEOUT.equals(_try.get().getStatusCode())) {
            return true;
        }
        return false;
    }

    private Boolean sendReport(Report report) {
        Try<ResponseEntity> _try = restClient.post(healthCheckReportEndpoint, report);
        if (_try.isSuccess() && HttpStatus.OK.equals(_try.get().getStatusCode())) {
            return true;
        }
        return false;
    }

}
