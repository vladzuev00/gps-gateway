package com.zuev.gpsgateway.model.lime;

public record LimeDataPackage(double latitude,
                              double longitude,
                              short speed,
                              short course,
                              float altitude,
                              byte satelliteCount,
                              Float hdop,
                              Byte battery) {
}
