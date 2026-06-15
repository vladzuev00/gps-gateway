package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;

//TODO remove
public abstract class PrefixedBinaryPackageDecoder<PREFIX> extends PrefixedPackageDecoder<ByteBuf, PREFIX> {

    public PrefixedBinaryPackageDecoder(PREFIX prefix) {
        super(prefix);
    }

    @Override
    protected final PREFIX getPrefix(ByteBuf byteBuf, int length) {
        byteBuf.markReaderIndex();
        PREFIX prefix = readPrefix(byteBuf, length);
        byteBuf.resetReaderIndex();
        return prefix;
    }

    @Override
    protected final ByteBuf skip(ByteBuf byteBuf, int length) {
        return byteBuf.skipBytes(length);
    }

    protected abstract PREFIX readPrefix(ByteBuf byteBuf, int length);
}
