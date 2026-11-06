package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoBlackBoxResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class MangoBlackBoxResponseEncoder extends MangoResponseEncoder<MangoBlackBoxResponse> {
    private static final byte TYPE = (byte) 0x84;

    public MangoBlackBoxResponseEncoder() {
        super(MangoBlackBoxResponse.class, TYPE);
    }

    @Override
    protected byte[] encodePayload(MangoBlackBoxResponse pkg) {
        return ByteBuffer.allocate(Short.BYTES)
                .putShort((short) pkg.messageCount())
                .array();
    }
}
