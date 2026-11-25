package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeDataResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class LimeDataResponseEncoder extends LimePackageEncoder<LimeDataResponse> {

    public LimeDataResponseEncoder() {
        super(LimeDataResponse.class);
    }

    @Override
    protected int getTagValuePairsLength(LimeDataResponse pkg) {
        return 0;
    }

    @Override
    protected void putTagValuePairs(ByteBuffer buffer, LimeDataResponse pkg) {
    }
}
