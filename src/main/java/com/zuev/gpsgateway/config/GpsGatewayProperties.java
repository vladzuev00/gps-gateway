package com.zuev.gpsgateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("gps-gateway")
public record GpsGatewayProperties(Protocols protocols) {

    public record Protocols(Protocol cherry, Protocol lime, Protocol mango) {
    }

    public record Protocol(int port, Duration readTimeout) {
    }
}
