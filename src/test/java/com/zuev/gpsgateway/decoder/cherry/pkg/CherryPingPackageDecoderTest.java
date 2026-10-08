package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryPingPackage;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static java.util.Collections.emptyIterator;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CherryPingPackageDecoderTest {
    private final CherryPingPackageDecoder decoder = new CherryPingPackageDecoder();

    @Test
    public void fieldsShouldBeDecoded() {
        Iterator<String> givenFields = emptyIterator();

        CherryPingPackage actual = decoder.decodeFields(givenFields);
        CherryPingPackage expected = new CherryPingPackage();
        assertEquals(expected, actual);
    }
}
