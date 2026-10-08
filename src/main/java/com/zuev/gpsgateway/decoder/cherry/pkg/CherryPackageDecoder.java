package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.decoder.base.pkg.PrefixedTextPackageDecoder;

public abstract class CherryPackageDecoder extends PrefixedTextPackageDecoder {
    private static final String FIELD_SEPARATOR = ";";
    private static final int KEEP_TRAILING_EMPTY_STRINGS = -1;

    public CherryPackageDecoder(String prefix) {
        super(prefix);
    }

    @Override
    protected final Object decodeBody(String body) {
        int lastFieldSeparatorIndex = body.lastIndexOf(FIELD_SEPARATOR);
        String[] fields = lastFieldSeparatorIndex != -1
                ? body.substring(0, lastFieldSeparatorIndex).split(FIELD_SEPARATOR, KEEP_TRAILING_EMPTY_STRINGS)
                : new String[0];
        return decodeFields(fields);
    }

    protected abstract Object decodeFields(String[] fields);
}
