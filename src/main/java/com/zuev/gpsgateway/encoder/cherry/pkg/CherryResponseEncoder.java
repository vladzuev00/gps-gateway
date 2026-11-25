package com.zuev.gpsgateway.encoder.cherry.pkg;

import com.zuev.gpsgateway.encoder.base.pkg.TextPackageEncoder;

import static java.nio.charset.StandardCharsets.US_ASCII;

public abstract class CherryResponseEncoder<PKG> extends TextPackageEncoder<PKG> {
    private static final String PACKAGE_END = "\0";

    private final String prefix;

    public CherryResponseEncoder(Class<PKG> packageType, String prefix) {
        super(packageType, US_ASCII);
        this.prefix = prefix;
    }

    @Override
    protected final String encodeToString(PKG pkg) {
        return prefix + encodePayload(pkg) + PACKAGE_END;
    }

    protected abstract String encodePayload(PKG pkg);
}
