package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;

public abstract class PrefixedBinaryPackageDecoder extends PrefixedPackageDecoder<ByteBuf, byte[]> {

    public PrefixedBinaryPackageDecoder(byte[] prefix) {
        super(prefix);
    }

    @Override
    protected final boolean startsWithPrefix(ByteBuf source, byte[] prefix) {
        for (int i = 0; i < prefix.length; i++) {
            if (source.getByte(source.readerIndex() + i) != prefix[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected final int getLength(byte[] prefix) {
        return prefix.length;
    }

    @Override
    protected final ByteBuf skip(ByteBuf source, int length) {
        return source.skipBytes(length);
    }
}
