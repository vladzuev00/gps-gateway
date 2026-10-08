package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryAuthPackage;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public final class CherryAuthPackageDecoder extends CherryPackageDecoder {
    private static final String PREFIX = "@AUTH@";

    public CherryAuthPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected CherryAuthPackage decodeFields(Iterator<String> iterator) {
        String imei = iterator.next();
        String password = iterator.next();
        return new CherryAuthPackage(imei, password);
    }
}
