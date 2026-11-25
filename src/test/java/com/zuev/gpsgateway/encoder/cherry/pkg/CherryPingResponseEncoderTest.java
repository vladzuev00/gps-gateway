package com.zuev.gpsgateway.encoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryPingResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CherryPingResponseEncoderTest {
    private final CherryPingResponseEncoder encoder = new CherryPingResponseEncoder();

    @Test
    public void payloadShouldBeEncoded() {
        CherryPingResponse givenPackage = new CherryPingResponse();

        String actual = encoder.encodePayload(givenPackage);
        String expected = "";
        assertEquals(expected, actual);
    }
}
