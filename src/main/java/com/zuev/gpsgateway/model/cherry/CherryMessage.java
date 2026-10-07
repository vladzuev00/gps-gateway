package com.zuev.gpsgateway.model.cherry;

import java.time.LocalDateTime;

public record CherryMessage(LocalDateTime dateTime,
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
