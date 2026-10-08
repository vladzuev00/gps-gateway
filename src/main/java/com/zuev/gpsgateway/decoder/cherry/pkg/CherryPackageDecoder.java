package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.decoder.base.pkg.PrefixedTextPackageDecoder;

import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public abstract class CherryPackageDecoder extends PrefixedTextPackageDecoder {
    private static final String FIELD_SEPARATOR = ";";
    private static final int NO_SUCH_OCCURRENCE = -1;
    private static final int KEEP_TRAILING_EMPTY_STRINGS = -1;

    public CherryPackageDecoder(String prefix) {
        super(prefix);
    }

    @Override
    protected final Object decodeBody(String body) {
        int lastFieldSeparatorIndex = body.lastIndexOf(FIELD_SEPARATOR);
        List<String> fields = lastFieldSeparatorIndex != NO_SUCH_OCCURRENCE
                ? asList(body.substring(0, lastFieldSeparatorIndex).split(FIELD_SEPARATOR, KEEP_TRAILING_EMPTY_STRINGS))
                : emptyList();
        return decodeFields(fields.iterator());
    }

    protected abstract Object decodeFields(Iterator<String> iterator);
}
