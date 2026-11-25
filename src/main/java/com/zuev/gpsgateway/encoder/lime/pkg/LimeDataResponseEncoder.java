package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeDataResponse;
import org.springframework.stereotype.Component;

@Component
public final class LimeDataResponseEncoder extends LimeResponseEncoder<LimeDataResponse> {

    public LimeDataResponseEncoder() {
        super(LimeDataResponse.class);
    }

    @Override
    protected byte[] encodeTagValuePairs(LimeDataResponse pkg) {
        return new byte[0];
    }
}
