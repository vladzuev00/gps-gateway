package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoPingResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class MangoPingResponseEncoder extends MangoResponseEncoder<MangoPingResponse> {
    private static final byte TYPE = (byte) 0x82;
    private static final int PAYLOAD_LENGTH = 0;

    public MangoPingResponseEncoder() {
        super(MangoPingResponse.class, TYPE, PAYLOAD_LENGTH);
    }

    @Override
    protected void putPayload(ByteBuffer buffer, MangoPingResponse pkg) {
    }
}
