package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimePingResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class LimePingResponseEncoderTest {
    private final LimePingResponseEncoder encoder = new LimePingResponseEncoder();

    @Test
    public void packageShouldBeEncoded() {
        LimePingResponse givenPackage = new LimePingResponse();

        byte[] actual = encoder.encode(givenPackage);
        byte[] expected = {0x00, 0x00};
        assertArrayEquals(expected, actual);
    }
}
