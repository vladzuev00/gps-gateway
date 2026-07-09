package com.zuev.gpsgateway.model.mango;

public record MangoData(long epochMillis,
                        double latitude,
                        double longitude,
                        Short speed,
                        Short course,
                        Float altitude,
                        Byte satelliteCount,
                        Float hdop,
                        Byte ignition,
                        Byte battery) {
}
