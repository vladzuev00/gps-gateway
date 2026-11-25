package com.zuev.gpsgateway.encoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryBlackBoxResponse;
import org.springframework.stereotype.Component;

@Component
public final class CherryBlackBoxResponseEncoder extends CherryResponseEncoder<CherryBlackBoxResponse> {
    private static final String PREFIX = "@ABLACKBOX@";

    public CherryBlackBoxResponseEncoder() {
        super(CherryBlackBoxResponse.class, PREFIX);
    }

    @Override
    protected String encodePayload(CherryBlackBoxResponse pkg) {
        return "";
    }
}
