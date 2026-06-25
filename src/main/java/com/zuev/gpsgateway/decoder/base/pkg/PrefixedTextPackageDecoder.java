package com.zuev.gpsgateway.decoder.base.pkg;

import java.util.Objects;

public abstract class PrefixedTextPackageDecoder extends PrefixedPackageDecoder<String, String> {

    public PrefixedTextPackageDecoder(String prefix) {
        super(prefix);
    }

    @Override
    protected final int getLength(String prefix) {
        return prefix.length();
    }

    @Override
    protected final String getPrefix(String source, int length) {
        return source.substring(0, length);
    }

    @Override
    protected final boolean equals(String firstPrefix, String secondPrefix) {
        return Objects.equals(firstPrefix, secondPrefix);
    }

    @Override
    protected final String skip(String source, int length) {
        return source.substring(length);
    }
}
