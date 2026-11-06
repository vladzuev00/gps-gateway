package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoBlackBoxResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class MangoBlackBoxResponseEncoderTest {
    private final MangoBlackBoxResponseEncoder encoder = new MangoBlackBoxResponseEncoder();

    @Test
    public void payloadShouldBeEncoded() {
        MangoBlackBoxResponse givenPackage = new MangoBlackBoxResponse(65535);

        byte[] actual = encoder.encodePayload(givenPackage);
        byte[] expected = {(byte) 0xFF, (byte) 0xFF};
        assertArrayEquals(expected, actual);
    }
}
