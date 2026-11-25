package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeDataResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class LimeDataResponseEncoderTest {
    private final LimeDataResponseEncoder encoder = new LimeDataResponseEncoder();

    @Test
    public void tagValuePairsShouldBeEncoded() {
        LimeDataResponse givenPackage = new LimeDataResponse();

        byte[] actual = encoder.encodeTagValuePairs(givenPackage);
        byte[] expected = {};
        assertArrayEquals(expected, actual);
    }
}
