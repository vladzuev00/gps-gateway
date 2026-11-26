package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeErrorResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class LimeErrorResponseEncoder extends LimePackageEncoder<LimeErrorResponse> {
    private static final int TAG_VALUE_PAIRS_LENGTH = Byte.BYTES + Byte.BYTES;
    private static final byte ERROR_TAG = 0x0D;

    public LimeErrorResponseEncoder() {
        super(LimeErrorResponse.class, TAG_VALUE_PAIRS_LENGTH);
    }

    @Override
    protected void putTagValuePairs(ByteBuffer buffer, LimeErrorResponse pkg) {
        buffer.put(ERROR_TAG);
        buffer.put(pkg.code());
    }
}
