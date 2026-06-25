package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;

public abstract class ByteArrayPrefixedBinaryPackageDecoder extends PrefixedBinaryPackageDecoder<byte[]> {

    public ByteArrayPrefixedBinaryPackageDecoder(byte[] bytes) {
        super(bytes);
    }

    @Override
    protected final byte[] getPrefix(ByteBuf source, int length) {
        byte[] bytes = new byte[length];
        source.getBytes(source.readerIndex(), bytes);
        return bytes;
    }

    @Override
    protected final int getLength(byte[] prefix) {
        return prefix.length;
    }

    @Override
    protected final boolean equals(byte[] firstPrefix, byte[] secondPrefix) {
        return Arrays.equals(firstPrefix, secondPrefix);
    }
}
