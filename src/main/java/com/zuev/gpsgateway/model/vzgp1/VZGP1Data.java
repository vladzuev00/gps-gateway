package com.zuev.gpsgateway.model.vzgp1;

public record VZGP1Data(long epochMillis,
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
