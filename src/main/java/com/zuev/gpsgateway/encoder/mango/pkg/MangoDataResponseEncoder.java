package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoDataResponse;
import org.springframework.stereotype.Component;

@Component
public final class MangoDataResponseEncoder extends MangoResponseEncoder<MangoDataResponse> {
    private static final byte TYPE = (byte) 0x83;

    public MangoDataResponseEncoder() {
        super(MangoDataResponse.class, TYPE);
    }

    @Override
    protected byte[] encodePayload(MangoDataResponse pkg) {
        return new byte[0];
    }
}
