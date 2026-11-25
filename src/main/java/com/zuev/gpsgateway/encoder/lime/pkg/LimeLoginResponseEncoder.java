package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeLoginResponse;
import org.springframework.stereotype.Component;

@Component
public final class LimeLoginResponseEncoder extends LimeResponseEncoder<LimeLoginResponse> {
    private static final byte STATUS_TAG = 0x03;

    public LimeLoginResponseEncoder() {
        super(LimeLoginResponse.class);
    }

    @Override
    protected byte[] encodeTagValuePairs(LimeLoginResponse pkg) {
        return new byte[]{STATUS_TAG, pkg.status()};
    }
}
