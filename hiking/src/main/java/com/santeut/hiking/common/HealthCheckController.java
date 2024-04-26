package com.santeut.hiking.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping(value = "/party/healthcheck")
    public HealthStatus healthcheck() {
        return new HealthStatus("ok");
    }

    class HealthStatus {
        private String status;
        HealthStatus(String status) {
            this.status = status;
        }
        String getStatus() {
            return status;
        }
    }

}
