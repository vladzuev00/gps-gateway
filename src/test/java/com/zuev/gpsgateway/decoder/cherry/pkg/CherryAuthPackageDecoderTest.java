package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryAuthPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CherryAuthPackageDecoderTest {
    private final CherryAuthPackageDecoder decoder = new CherryAuthPackageDecoder();

    @Test
    public void fieldsShouldBeDecoded() {
        String[] givenFields = {"123456789012345", "pass"};

        CherryAuthPackage actual = decoder.decodeFields(givenFields);
        CherryAuthPackage expected = new CherryAuthPackage("123456789012345", "pass");
        assertEquals(expected, actual);
    }
}
