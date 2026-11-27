package com.zuev.gpsgateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import static org.springframework.boot.SpringApplication.run;

@ConfigurationPropertiesScan
@SpringBootApplication
public class GpsGatewayApplication {

    public static void main(String[] args) {
        run(GpsGatewayApplication.class, args);
    }
}
