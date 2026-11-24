package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoLoginResponse;
import org.springframework.stereotype.Component;

@Component
public final class MangoLoginResponseEncoder extends MangoResponseEncoder<MangoLoginResponse> {
    private static final byte TYPE = (byte) 0x81;

    public MangoLoginResponseEncoder() {
        super(MangoLoginResponse.class, TYPE);
    }

    @Override
    protected byte[] encodePayload(MangoLoginResponse pkg) {
        return new byte[]{pkg.status()};
    }
}
