package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.encoder.base.pkg.PackageEncoder;

import static java.nio.ByteBuffer.allocate;

public abstract class MangoResponsePackageEncoder<PKG> extends PackageEncoder<PKG> {
    private static final int PAYLOAD_LENGTH_LENGTH = 2;

    private final byte[] prefix;

    public MangoResponsePackageEncoder(Class<PKG> packageType, byte[] prefix) {
        super(packageType);
        this.prefix = prefix;
    }

    @Override
    protected final byte[] encodeInternal(PKG pkg) {
        byte[] payload = encodePayload(pkg);
        return allocate(prefix.length + PAYLOAD_LENGTH_LENGTH + payload.length)
                .put(prefix)
                .putShort((short) payload.length)
                .put(payload)
                .array();
    }

    protected abstract byte[] encodePayload(PKG pkg);
}
