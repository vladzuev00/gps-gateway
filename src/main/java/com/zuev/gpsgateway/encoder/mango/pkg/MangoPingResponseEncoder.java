package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoPingResponse;
import org.springframework.stereotype.Component;

@Component
public final class MangoPingResponseEncoder extends MangoResponseEncoder<MangoPingResponse> {
    private static final byte TYPE = (byte) 0x82;

    public MangoPingResponseEncoder() {
        super(MangoPingResponse.class, TYPE);
    }

    @Override
    protected byte[] encodePayload(MangoPingResponse pkg) {
        return new byte[0];
    }
}
