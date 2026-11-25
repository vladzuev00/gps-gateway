package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeDataResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class LimeDataResponseEncoderTest {
    private final LimeDataResponseEncoder encoder = new LimeDataResponseEncoder();

    @Test
    public void packageShouldBeEncoded() {
        LimeDataResponse givenPackage = new LimeDataResponse();

        byte[] actual = encoder.encode(givenPackage);
        byte[] expected = {0x00, 0x00};
        assertArrayEquals(expected, actual);
    }
}
