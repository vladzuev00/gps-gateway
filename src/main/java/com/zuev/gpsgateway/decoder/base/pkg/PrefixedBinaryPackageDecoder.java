package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;

public abstract class PrefixedBinaryPackageDecoder<PREFIX> extends PrefixedPackageDecoder<ByteBuf, PREFIX> {

    public PrefixedBinaryPackageDecoder(PREFIX prefix) {
        super(prefix);
    }

    @Override
    protected final ByteBuf skip(ByteBuf source, int length) {
        return source.skipBytes(length);
    }
}
