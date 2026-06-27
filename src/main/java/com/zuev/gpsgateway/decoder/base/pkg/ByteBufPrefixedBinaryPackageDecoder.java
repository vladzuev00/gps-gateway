package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;

import java.util.Objects;

public abstract class ByteBufPrefixedBinaryPackageDecoder extends PrefixedBinaryPackageDecoder<ByteBuf> {

    public ByteBufPrefixedBinaryPackageDecoder(ByteBuf prefix) {
        super(prefix);
    }

    @Override
    protected final int getLength(ByteBuf prefix) {
        return prefix.readableBytes();
    }

    @Override
    protected final ByteBuf getPrefix(ByteBuf source, int length) {
        return source.slice(source.readerIndex(), length);
    }

    @Override
    protected final boolean equals(ByteBuf firstPrefix, ByteBuf secondPrefix) {
        return Objects.equals(firstPrefix, secondPrefix);
    }
}
