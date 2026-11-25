package com.zuev.gpsgateway.encoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryLoginResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CherryLoginResponseEncoderTest {
    private final CherryLoginResponseEncoder encoder = new CherryLoginResponseEncoder();

    @Test
    public void payloadShouldBeEncoded() {
        CherryLoginResponse givenPackage = new CherryLoginResponse((byte) 127);

        String actual = encoder.encodePayload(givenPackage);
        String expected = "127";
        assertEquals(expected, actual);
    }
}
