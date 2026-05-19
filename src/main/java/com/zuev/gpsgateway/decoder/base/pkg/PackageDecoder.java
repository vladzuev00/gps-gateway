package com.zuev.gpsgateway.decoder.base.pkg;

public interface PackageDecoder<PKG_SRC> {
    boolean isAbleDecode(PKG_SRC source);

    Object decode(PKG_SRC source);
}
