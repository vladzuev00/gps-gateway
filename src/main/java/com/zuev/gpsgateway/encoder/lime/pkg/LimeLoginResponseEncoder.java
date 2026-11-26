package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeLoginResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class LimeLoginResponseEncoder extends LimePackageEncoder<LimeLoginResponse> {
    private static final int TAG_VALUE_PAIRS_LENGTH = Byte.BYTES + Byte.BYTES;
    private static final byte STATUS_TAG = 0x03;

    public LimeLoginResponseEncoder() {
        super(LimeLoginResponse.class, TAG_VALUE_PAIRS_LENGTH);
    }

    @Override
    protected void putTagValuePairs(ByteBuffer buffer, LimeLoginResponse pkg) {
        buffer.put(STATUS_TAG);
        buffer.put(pkg.status());
    }
}
