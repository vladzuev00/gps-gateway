package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimePingResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class LimePingResponseEncoder extends LimePackageEncoder<LimePingResponse> {
    private static final int TAG_VALUE_PAIRS_LENGTH = 0;

    public LimePingResponseEncoder() {
        super(LimePingResponse.class, TAG_VALUE_PAIRS_LENGTH);
    }

    @Override
    protected void putTagValuePairs(ByteBuffer buffer, LimePingResponse pkg) {
    }
}
