package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.encoder.base.pkg.PackageEncoder;

import java.nio.ByteBuffer;

public abstract class MangoResponseEncoder<PKG> extends PackageEncoder<PKG> {
    private static final byte[] PREFIX = {0x56, 0x5A};

    private final byte type;

    public MangoResponseEncoder(Class<PKG> packageType, byte type) {
        super(packageType);
        this.type = type;
    }

    @Override
    protected final byte[] encodeInternal(PKG pkg) {
        byte[] encodedPayload = encodePayload(pkg);
        return ByteBuffer.allocate(PREFIX.length + Byte.BYTES + Short.BYTES + encodedPayload.length)
                .put(PREFIX)
                .put(type)
                .putShort((short) encodedPayload.length)
                .put(encodedPayload)
                .array();
    }

    protected abstract byte[] encodePayload(PKG pkg);
}
