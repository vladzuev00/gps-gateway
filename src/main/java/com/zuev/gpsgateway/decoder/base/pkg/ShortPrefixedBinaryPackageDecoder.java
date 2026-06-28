package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;

public abstract class ShortPrefixedBinaryPackageDecoder implements PackageDecoder<ByteBuf> {
    private final short prefix;

    public ShortPrefixedBinaryPackageDecoder(short prefix) {
        this.prefix = prefix;
    }

    @Override
    public final boolean isAbleDecode(ByteBuf source) {
        return source.getShort(source.readerIndex()) == prefix;
    }

    @Override
    public final Object decode(ByteBuf source) {
        source.skipBytes(Short.BYTES);
        return decodeBody(source);
    }

    protected abstract Object decodeBody(ByteBuf body);
}
