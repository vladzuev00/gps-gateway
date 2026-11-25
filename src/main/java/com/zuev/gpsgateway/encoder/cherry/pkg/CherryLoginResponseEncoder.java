package com.zuev.gpsgateway.encoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryLoginResponse;
import org.springframework.stereotype.Component;

@Component
public final class CherryLoginResponseEncoder extends CherryResponseEncoder<CherryLoginResponse> {
    private static final String PREFIX = "@ALOGIN@";

    public CherryLoginResponseEncoder() {
        super(CherryLoginResponse.class, PREFIX);
    }

    @Override
    protected String encodePayload(CherryLoginResponse pkg) {
        return Byte.toString(pkg.status());
    }
}
