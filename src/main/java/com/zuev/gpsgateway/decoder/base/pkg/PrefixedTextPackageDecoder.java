package com.zuev.gpsgateway.decoder.base.pkg;

public abstract class PrefixedTextPackageDecoder extends PrefixedPackageDecoder<String, String> {

    public PrefixedTextPackageDecoder(String prefix) {
        super(prefix);
    }

    @Override
    protected final boolean startsWithPrefix(String source, String prefix) {
        return source.startsWith(prefix);
    }

    @Override
    protected final int getLength(String prefix) {
        return prefix.length();
    }

    @Override
    protected final String skip(String source, int length) {
        return source.substring(length);
    }
}
