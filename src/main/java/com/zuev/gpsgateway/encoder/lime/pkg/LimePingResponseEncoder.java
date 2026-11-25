package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimePingResponse;
import org.springframework.stereotype.Component;

@Component
public final class LimePingResponseEncoder extends LimeResponseEncoder<LimePingResponse> {

    public LimePingResponseEncoder() {
        super(LimePingResponse.class);
    }

    @Override
    protected byte[] encodeTagValuePairs(LimePingResponse pkg) {
        return new byte[0];
    }
}
