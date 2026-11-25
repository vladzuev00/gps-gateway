package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoDataResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class MangoDataResponseEncoder extends MangoResponseEncoder<MangoDataResponse> {
    private static final byte TYPE = (byte) 0x83;
    private static final int PAYLOAD_LENGTH = 0;

    public MangoDataResponseEncoder() {
        super(MangoDataResponse.class, TYPE, PAYLOAD_LENGTH);
    }

    @Override
    protected void putPayload(ByteBuffer buffer, MangoDataResponse pkg) {
    }
}
