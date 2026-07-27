package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.decoder.base.pkg.PrefixedBinaryPackageDecoder;
import io.netty.buffer.ByteBuf;

public abstract class MangoPackageDecoder extends PrefixedBinaryPackageDecoder {
    private static final int CHECKSUM_LENGTH = 2;

    public MangoPackageDecoder(byte[] prefix) {
        super(prefix);
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
