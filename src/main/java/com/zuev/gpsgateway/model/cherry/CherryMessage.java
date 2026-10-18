package com.zuev.gpsgateway.model.cherry;

import java.time.Instant;

//TODO сделать другие доп параметры, эти совпадают с mango
public record CherryMessage(Instant dateTime,
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
