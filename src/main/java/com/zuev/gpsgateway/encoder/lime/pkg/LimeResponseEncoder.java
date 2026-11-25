package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.encoder.base.pkg.PackageEncoder;

import java.nio.ByteBuffer;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

public abstract class LimeResponseEncoder<PKG> extends PackageEncoder<PKG> {

    public LimeResponseEncoder(Class<PKG> packageType) {
        super(packageType);
    }

    @Override
    protected final byte[] encodeInternal(PKG pkg) {
        byte[] encodedTagValuePairs = encodeTagValuePairs(pkg);
        return ByteBuffer.allocate(Short.BYTES + encodedTagValuePairs.length)
                .order(LITTLE_ENDIAN)
                .putShort((short) encodedTagValuePairs.length)
                .put(encodedTagValuePairs)
                .array();
    }

    protected abstract byte[] encodeTagValuePairs(PKG pkg);
}
