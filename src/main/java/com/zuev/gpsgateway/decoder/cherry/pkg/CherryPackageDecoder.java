package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.decoder.base.pkg.PrefixedTextPackageDecoder;

import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public abstract class CherryPackageDecoder extends PrefixedTextPackageDecoder {
    private static final String FIELD_END = ";";
    private static final int NO_SUCH_OCCURRENCE = -1;
    private static final int KEEP_TRAILING_EMPTY_STRINGS = -1;

    public CherryPackageDecoder(String prefix) {
        super(prefix);
    }

    @Override
    protected final Object decodeBody(String body) {
        int lastFieldEndIndex = body.lastIndexOf(FIELD_END);
        List<String> fields = lastFieldEndIndex != NO_SUCH_OCCURRENCE
                ? asList(body.substring(0, lastFieldEndIndex).split(FIELD_END, KEEP_TRAILING_EMPTY_STRINGS))
                : emptyList();
        return decodeFields(fields.iterator());
    }

    protected abstract Object decodeFields(Iterator<String> iterator);
}
