package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public abstract class ByteBufPrefixedBinaryPackageDecoder extends PrefixedPackageDecoder<ByteBuf, ByteBuf> {

    public ByteBufPrefixedBinaryPackageDecoder(ByteBuf prefix) {
        super(prefix);
    }

    @Override
    protected final boolean startsWithPrefix(ByteBuf source, ByteBuf prefix) {
        return ByteBufUtil.equals(source, source.readerIndex(), prefix, prefix.readerIndex(), prefix.readableBytes());
    }

    @Override
    protected final int getLength(ByteBuf prefix) {
        return prefix.readableBytes();
    }

    @Override
    protected final ByteBuf skip(ByteBuf source, int length) {
        return source.skipBytes(length);
    }
}
