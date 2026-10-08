package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryPingPackage;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public final class CherryPingPackageDecoder extends CherryPackageDecoder {
    private static final String PREFIX = "@PING@";

    public CherryPingPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected CherryPingPackage decodeFields(Iterator<String> iterator) {
        return new CherryPingPackage();
    }
}
