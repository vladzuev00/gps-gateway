package com.zuev.gpsgateway.encoder.base.pkg;

import java.nio.charset.Charset;

public abstract class TextPackageEncoder<PKG> extends PackageEncoder<PKG> {
    private final Charset charset;

    public TextPackageEncoder(Class<PKG> packageType, Charset charset) {
        super(packageType);
        this.charset = charset;
    }

    @Override
    protected final byte[] encodeInternal(PKG pkg) {
        return encodeToString(pkg).getBytes(charset);
    }

    protected abstract String encodeToString(PKG pkg);
}
