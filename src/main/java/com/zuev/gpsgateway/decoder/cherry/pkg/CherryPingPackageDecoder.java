package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryPingPackage;
import org.springframework.stereotype.Component;

@Component
public final class CherryPingPackageDecoder extends CherryPackageDecoder {
    private static final String PREFIX = "@PING@";

    public CherryPingPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected CherryPingPackage decodeFields(String[] fields) {
        return new CherryPingPackage();
    }
}
