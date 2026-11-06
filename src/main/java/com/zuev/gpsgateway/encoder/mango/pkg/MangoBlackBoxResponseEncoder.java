package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoBlackBoxResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class MangoBlackBoxResponseEncoder extends MangoResponseEncoder<MangoBlackBoxResponse> {
    private static final byte[] PREFIX = {0x56, 0x5A, (byte) 0x84};

    public MangoBlackBoxResponseEncoder() {
        super(MangoBlackBoxResponse.class, PREFIX);
    }

    @Override
    protected byte[] encodePayload(MangoBlackBoxResponse pkg) {
        return ByteBuffer.allocate(Short.BYTES)
                .putShort((short) pkg.messageCount())
                .array();
    }
}
