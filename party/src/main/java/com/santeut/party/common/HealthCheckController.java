package com.santeut.party.common;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping(value = "/party/healthcheck")
    public HealthStatus healthcheck() {
//        return "ok";
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
