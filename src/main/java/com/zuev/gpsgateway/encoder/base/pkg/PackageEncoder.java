package com.zuev.gpsgateway.encoder.base.pkg;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PackageEncoder<PKG> {
    private final Class<PKG> packageType;

    public final boolean isAbleEncode(Object pkg) {
        return packageType.isInstance(pkg);
    }

    public final byte[] encode(Object pkg) {
        PKG castedPackage = packageType.cast(pkg);
        return encodeInternal(castedPackage);
    }

    protected abstract byte[] encodeInternal(PKG pkg);
}
