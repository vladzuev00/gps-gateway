package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimePingResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class LimePingResponseEncoderTest {
    private final LimePingResponseEncoder encoder = new LimePingResponseEncoder();

    @Test
    public void tagValuePairsShouldBeEncoded() {
        LimePingResponse givenPackage = new LimePingResponse();

        byte[] actual = encoder.encodeTagValuePairs(givenPackage);
        byte[] expected = {};
        assertArrayEquals(expected, actual);
    }
}
