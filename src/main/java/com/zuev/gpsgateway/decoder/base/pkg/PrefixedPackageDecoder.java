package com.zuev.gpsgateway.decoder.base.pkg;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PrefixedPackageDecoder<PKG_SRC, PREFIX> implements PackageDecoder<PKG_SRC> {
    private final PREFIX prefix;

    @Override
    public final boolean isAbleDecode(PKG_SRC source) {
        int prefixLength = getLength(prefix);
        PREFIX sourcePrefix = getPrefix(source, prefixLength);
        return equals(sourcePrefix, prefix);
    }

    @Override
    public final Object decode(PKG_SRC source) {
        int prefixLength = getLength(prefix);
        PKG_SRC body = skip(source, prefixLength);
        return decodeBody(body);
    }

    protected abstract int getLength(PREFIX prefix);

    protected abstract PREFIX getPrefix(PKG_SRC source, int length);

    protected abstract boolean equals(PREFIX first, PREFIX second);

    protected abstract PKG_SRC skip(PKG_SRC source, int length);

    protected abstract Object decodeBody(PKG_SRC source);
}
