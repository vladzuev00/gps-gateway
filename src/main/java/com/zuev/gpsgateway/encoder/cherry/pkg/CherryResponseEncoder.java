package com.zuev.gpsgateway.encoder.cherry.pkg;

import com.zuev.gpsgateway.encoder.base.pkg.TextPackageEncoder;

import static java.nio.charset.StandardCharsets.US_ASCII;

public abstract class CherryResponseEncoder<PKG> extends TextPackageEncoder<PKG> {
    private static final String TYPE_SEPARATOR = "@";
    private static final String PACKAGE_END = "\0";

    private final String type;

    public CherryResponseEncoder(Class<PKG> packageType, String type) {
        super(packageType, US_ASCII);
        this.type = type;
    }

    @Override
    protected final String encodeToString(PKG pkg) {
        return TYPE_SEPARATOR + type + TYPE_SEPARATOR + encodePayload(pkg) + PACKAGE_END;
    }

    protected abstract String encodePayload(PKG pkg);
}
