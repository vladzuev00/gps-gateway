package com.zuev.gpsgateway.model;

import java.util.Map;

public record Message(double longitude,
                      double latitude,
                      long epochMillis,
                      Double speed,
                      Double course,
                      Double altitude,
                      Integer satelliteCount,
                      Double hdop,
                      Map<String, String> additionalParametersByNames,
                      long trackerId
) {
}
