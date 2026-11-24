package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoLoginResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class MangoLoginResponseEncoderTest {
    private final MangoLoginResponseEncoder encoder = new MangoLoginResponseEncoder();

    @Test
    public void payloadShouldBeEncoded() {
        MangoLoginResponse givenPackage = new MangoLoginResponse((byte) 0x02);

        byte[] actual = encoder.encodePayload(givenPackage);
        byte[] expected = {0x02};
        assertArrayEquals(expected, actual);
    }
}
