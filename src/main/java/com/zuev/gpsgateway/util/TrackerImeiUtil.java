package com.zuev.gpsgateway.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

//TODO refactor and test
@NoArgsConstructor(access = PRIVATE)
public final class TrackerImeiUtil {
    private static final int LENGTH = 20;
    private static final char PADDING_CHAR = '0';

    public static String normalizeImei(String imei) {
        if (imei.length() > LENGTH) {
            throw new IllegalArgumentException("Imei '%s' is longer than %d characters".formatted(imei, LENGTH));
        }
        return String.valueOf(PADDING_CHAR).repeat(LENGTH - imei.length()) + imei;
    }
}
