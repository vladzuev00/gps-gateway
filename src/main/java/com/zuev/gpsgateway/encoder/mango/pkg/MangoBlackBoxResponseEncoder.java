package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoBlackBoxResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class MangoBlackBoxResponseEncoder extends MangoResponseEncoder<MangoBlackBoxResponse> {
    private static final byte TYPE = (byte) 0x84;
    private static final int PAYLOAD_LENGTH = Short.BYTES;

    public MangoBlackBoxResponseEncoder() {
        super(MangoBlackBoxResponse.class, TYPE, PAYLOAD_LENGTH);
    }

    @Override
    protected void putPayload(ByteBuffer buffer, MangoBlackBoxResponse pkg) {
        buffer.putShort((short) pkg.messageCount());
    }
}
