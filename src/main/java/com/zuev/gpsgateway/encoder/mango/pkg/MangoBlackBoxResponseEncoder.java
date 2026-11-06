package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoBlackBoxResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class MangoBlackBoxResponseEncoder extends MangoResponsePackageEncoder<MangoBlackBoxResponse> {
    private static final byte[] PREFIX = {0x56, 0x5A, (byte) 0x84};
    private static final int MESSAGE_COUNT_LENGTH = 2;

    public MangoBlackBoxResponseEncoder() {
        super(MangoBlackBoxResponse.class, PREFIX);
    }

    @Override
    protected byte[] encodePayload(MangoBlackBoxResponse pkg) {
        return ByteBuffer.allocate(MESSAGE_COUNT_LENGTH)
                .putShort((short) pkg.messageCount())
                .array();
    }
}
