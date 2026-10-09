package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryPingPackage;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class CherryPingPackageDecoderTest {
    private final CherryPingPackageDecoder decoder = new CherryPingPackageDecoder();

    @Test
    public void fieldsShouldBeDecoded() {
        @SuppressWarnings("unchecked") Iterator<String> givenIterator = mock(Iterator.class);

        CherryPingPackage actual = decoder.decodeFields(givenIterator);
        CherryPingPackage expected = new CherryPingPackage();
        assertEquals(expected, actual);

        verifyNoInteractions(givenIterator);
    }
}
