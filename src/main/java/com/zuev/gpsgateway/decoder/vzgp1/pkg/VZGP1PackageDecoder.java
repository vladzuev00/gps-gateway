package com.zuev.gpsgateway.decoder.vzgp1.pkg;

import com.zuev.gpsgateway.decoder.base.pkg.ByteArrayPrefixedBinaryPackageDecoder;
import io.netty.buffer.ByteBuf;

public abstract class VZGP1PackageDecoder extends ByteArrayPrefixedBinaryPackageDecoder {
    private static final int CHECKSUM_LENGTH = 2;

    public VZGP1PackageDecoder(byte[] prefix) {
        super(prefix);
    }

    @Override
    protected final Object decodeBody(ByteBuf byteBuf) {
        int payloadLength = byteBuf.readUnsignedShort();
        ByteBuf payloadSlice = byteBuf.readSlice(payloadLength);
        byteBuf.skipBytes(CHECKSUM_LENGTH);
        return decodePayload(payloadSlice);
    }

    protected abstract Object decodePayload(ByteBuf byteBuf);
}
