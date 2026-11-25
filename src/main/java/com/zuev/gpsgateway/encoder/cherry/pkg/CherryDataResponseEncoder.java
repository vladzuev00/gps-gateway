package com.zuev.gpsgateway.encoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryDataResponse;
import org.springframework.stereotype.Component;

@Component
public final class CherryDataResponseEncoder extends CherryResponseEncoder<CherryDataResponse> {
    private static final String PREFIX = "@ADATA@";

    public CherryDataResponseEncoder() {
        super(CherryDataResponse.class, PREFIX);
    }

    @Override
    protected String encodePayload(CherryDataResponse pkg) {
        return "";
    }
}
