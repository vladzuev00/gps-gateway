package com.zuev.gpsgateway.decoder.vzgp1.pkg;

import com.zuev.gpsgateway.decoder.base.pkg.ByteBufPrefixedBinaryPackageDecoder;
import io.netty.buffer.ByteBuf;

public abstract class VZGP1PackageDecoder extends ByteBufPrefixedBinaryPackageDecoder {
    private static final int CHECKSUM_LENGTH = 2;

    public VZGP1PackageDecoder(ByteBuf prefix) {
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
