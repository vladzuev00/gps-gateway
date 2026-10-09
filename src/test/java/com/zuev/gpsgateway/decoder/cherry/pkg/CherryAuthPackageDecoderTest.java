package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryAuthPackage;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public final class CherryAuthPackageDecoderTest {
    private final CherryAuthPackageDecoder decoder = new CherryAuthPackageDecoder();

    @Test
    public void fieldsShouldBeDecoded() {
        Iterator<String> givenIterator = List.of("123456789012345", "pass").iterator();

        CherryAuthPackage actual = decoder.decodeFields(givenIterator);
        CherryAuthPackage expected = new CherryAuthPackage("123456789012345", "pass");
        assertEquals(expected, actual);
        assertFalse(givenIterator.hasNext());
    }
}
