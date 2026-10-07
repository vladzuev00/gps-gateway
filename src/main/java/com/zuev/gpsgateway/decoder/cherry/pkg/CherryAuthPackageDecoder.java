package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryAuthPackage;
import org.springframework.stereotype.Component;

@Component
public final class CherryAuthPackageDecoder extends CherryPackageDecoder {
    private static final String PREFIX = "@AUTH@";
    private static final int IMEI_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;

    public CherryAuthPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected CherryAuthPackage decodeFields(String[] fields) {
        String imei = fields[IMEI_INDEX];
        String password = fields[PASSWORD_INDEX];
        return new CherryAuthPackage(imei, password);
    }
}
