package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.encoder.base.pkg.PackageEncoder;

import java.nio.ByteBuffer;

public abstract class MangoResponseEncoder<PKG> extends PackageEncoder<PKG> {
    private final byte[] prefix;

    public MangoResponseEncoder(Class<PKG> packageType, byte[] prefix) {
        super(packageType);
        this.prefix = prefix;
    }

    @Override
    protected final byte[] encodeInternal(PKG pkg) {
        byte[] encodedPayload = encodePayload(pkg);
        return ByteBuffer.allocate(prefix.length + Short.BYTES + encodedPayload.length)
                .put(prefix)
                .putShort((short) encodedPayload.length)
                .put(encodedPayload)
                .array();
    }

    protected abstract byte[] encodePayload(PKG pkg);
}
