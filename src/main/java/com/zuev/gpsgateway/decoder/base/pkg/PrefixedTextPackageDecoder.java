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
    protected final String getPrefix(String text, int length) {
        return text.substring(0, length);
    }

    @Override
    protected final boolean equals(String first, String second) {
        return Objects.equals(first, second);
    }

    @Override
    protected final String skip(String text, int length) {
        return text.substring(length);
    }
}
