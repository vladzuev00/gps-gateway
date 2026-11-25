package com.zuev.gpsgateway.encoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryBlackBoxResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CherryBlackBoxResponseEncoderTest {
    private final CherryBlackBoxResponseEncoder encoder = new CherryBlackBoxResponseEncoder();

    @Test
    public void payloadShouldBeEncoded() {
        CherryBlackBoxResponse givenPackage = new CherryBlackBoxResponse();

        String actual = encoder.encodePayload(givenPackage);
        String expected = "";
        assertEquals(expected, actual);
    }
}
