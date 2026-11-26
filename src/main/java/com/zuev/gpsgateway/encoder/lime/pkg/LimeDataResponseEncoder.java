package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeDataResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class LimeDataResponseEncoder extends LimePackageEncoder<LimeDataResponse> {
    private static final int TAG_VALUE_PAIRS_LENGTH = 0;

    public LimeDataResponseEncoder() {
        super(LimeDataResponse.class, TAG_VALUE_PAIRS_LENGTH);
    }

    @Override
    protected void putTagValuePairs(ByteBuffer buffer, LimeDataResponse pkg) {
    }
}
