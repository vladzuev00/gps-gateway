package com.zuev.gpsgateway.decoder.base.pkg;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PrefixedPackageDecoder<PKG_SRC, PREFIX> implements PackageDecoder<PKG_SRC> {
    private final PREFIX prefix;

    @Override
    public final boolean isAbleDecode(PKG_SRC source) {
        return startsWithPrefix(source, prefix);
    }

    @Override
    public final Object decode(PKG_SRC source) {
        int prefixLength = getLength(prefix);
        PKG_SRC body = skip(source, prefixLength);
        return decodeBody(body);
    }

    protected abstract boolean startsWithPrefix(PKG_SRC source, PREFIX prefix);

    protected abstract int getLength(PREFIX prefix);

    protected abstract PKG_SRC skip(PKG_SRC source, int length);

    protected abstract Object decodeBody(PKG_SRC body);
}
