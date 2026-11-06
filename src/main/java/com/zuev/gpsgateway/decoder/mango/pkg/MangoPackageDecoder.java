package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.decoder.base.pkg.PrefixedBinaryPackageDecoder;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

public abstract class MangoPackageDecoder extends PrefixedBinaryPackageDecoder {
    private static final byte[] PREFIX = {0x56, 0x5A};
    private static final int CHECKSUM_LENGTH = 2;

    public MangoPackageDecoder(byte type) {
        super(ByteBuffer.allocate(PREFIX.length + 1).put(PREFIX).put(type).array());
    }

    @Override
    protected final Object decodeBody(ByteBuf body) {
        int payloadLength = body.readUnsignedShort();
        ByteBuf payload = body.readSlice(payloadLength);
        body.skipBytes(CHECKSUM_LENGTH);
        return decodePayload(payload);
    }

    protected abstract Object decodePayload(ByteBuf payload);
}
