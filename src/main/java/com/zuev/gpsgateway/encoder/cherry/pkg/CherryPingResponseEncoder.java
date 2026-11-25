package com.zuev.gpsgateway.encoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryPingResponse;
import org.springframework.stereotype.Component;

@Component
public final class CherryPingResponseEncoder extends CherryResponseEncoder<CherryPingResponse> {
    private static final String PREFIX = "@APING@";

    public CherryPingResponseEncoder() {
        super(CherryPingResponse.class, PREFIX);
    }

    @Override
    protected String encodePayload(CherryPingResponse pkg) {
        return "";
    }
}
