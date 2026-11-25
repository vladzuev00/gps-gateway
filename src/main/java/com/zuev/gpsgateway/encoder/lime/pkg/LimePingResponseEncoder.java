package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimePingResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class LimePingResponseEncoder extends LimePackageEncoder<LimePingResponse> {

    public LimePingResponseEncoder() {
        super(LimePingResponse.class);
    }

    @Override
    protected int getTagValuePairsLength(LimePingResponse pkg) {
        return 0;
    }

    @Override
    protected void putTagValuePairs(ByteBuffer buffer, LimePingResponse pkg) {
    }
}
