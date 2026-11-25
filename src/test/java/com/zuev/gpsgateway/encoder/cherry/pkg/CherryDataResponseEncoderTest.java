package com.zuev.gpsgateway.encoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryDataResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CherryDataResponseEncoderTest {
    private final CherryDataResponseEncoder encoder = new CherryDataResponseEncoder();

    @Test
    public void payloadShouldBeEncoded() {
        CherryDataResponse givenPackage = new CherryDataResponse();

        String actual = encoder.encodePayload(givenPackage);
        String expected = "";
        assertEquals(expected, actual);
    }
}
