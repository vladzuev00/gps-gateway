package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.encoder.base.pkg.PackageEncoder;

import java.nio.ByteBuffer;

public abstract class MangoResponseEncoder<PKG> extends PackageEncoder<PKG> {
    private static final byte[] PREFIX = {0x56, 0x5A};

    private final byte type;
    private final int payloadLength;

    public MangoResponseEncoder(Class<PKG> packageType, byte type, int payloadLength) {
        super(packageType);
        this.type = type;
        this.payloadLength = payloadLength;
    }

    @Override
    protected final byte[] encodeInternal(PKG pkg) {
        ByteBuffer buffer = ByteBuffer.allocate(PREFIX.length + Byte.BYTES + Short.BYTES + payloadLength);
        buffer.put(PREFIX);
        buffer.put(type);
        buffer.putShort((short) payloadLength);
        putPayload(buffer, pkg);
        return buffer.array();
    }

    protected abstract void putPayload(ByteBuffer buffer, PKG pkg);
}
