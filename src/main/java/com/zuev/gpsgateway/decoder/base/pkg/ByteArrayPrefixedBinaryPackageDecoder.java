package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;

public abstract class ByteArrayPrefixedBinaryPackageDecoder extends PrefixedBinaryPackageDecoder<byte[]> {

    public ByteArrayPrefixedBinaryPackageDecoder(byte[] bytes) {
        super(bytes);
    }

    @Override
    protected final byte[] readPrefix(ByteBuf byteBuf, int length) {
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    @Override
    protected final int getLength(byte[] bytes) {
        return bytes.length;
    }

    @Override
    protected final boolean equals(byte[] first, byte[] second) {
        return Arrays.equals(first, second);
    }
}
