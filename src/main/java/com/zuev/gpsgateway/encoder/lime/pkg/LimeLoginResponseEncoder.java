package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeLoginResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class LimeLoginResponseEncoder extends LimePackageEncoder<LimeLoginResponse> {
    private static final byte STATUS_TAG = 0x03;

    public LimeLoginResponseEncoder() {
        super(LimeLoginResponse.class);
    }

    @Override
    protected int getTagValuePairsLength(LimeLoginResponse pkg) {
        return Byte.BYTES + Byte.BYTES;
    }

    @Override
    protected void putTagValuePairs(ByteBuffer buffer, LimeLoginResponse pkg) {
        buffer.put(STATUS_TAG);
        buffer.put(pkg.status());
    }
}
