package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryLoginPackage;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public final class CherryLoginPackageDecoderTest {
    private final CherryLoginPackageDecoder decoder = new CherryLoginPackageDecoder();

    @Test
    public void fieldsShouldBeDecoded() {
        Iterator<String> givenIterator = List.of("123456789012345").iterator();

        CherryLoginPackage actual = decoder.decodeFields(givenIterator);
        CherryLoginPackage expected = new CherryLoginPackage("123456789012345");
        assertEquals(expected, actual);
        assertFalse(givenIterator.hasNext());
    }
}
