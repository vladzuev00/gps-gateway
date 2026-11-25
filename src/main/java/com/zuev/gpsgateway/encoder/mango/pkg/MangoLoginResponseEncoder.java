package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoLoginResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class MangoLoginResponseEncoder extends MangoResponseEncoder<MangoLoginResponse> {
    private static final byte TYPE = (byte) 0x81;
    private static final int PAYLOAD_LENGTH = Byte.BYTES;

    public MangoLoginResponseEncoder() {
        super(MangoLoginResponse.class, TYPE, PAYLOAD_LENGTH);
    }

    @Override
    protected void putPayload(ByteBuffer buffer, MangoLoginResponse pkg) {
        buffer.put(pkg.status());
    }
}
