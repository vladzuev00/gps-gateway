package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeLoginResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class LimeLoginResponseEncoderTest {
    private final LimeLoginResponseEncoder encoder = new LimeLoginResponseEncoder();

    @Test
    public void tagValuePairsShouldBeEncoded() {
        LimeLoginResponse givenPackage = new LimeLoginResponse((byte) 0x02);

        byte[] actual = encoder.encodeTagValuePairs(givenPackage);
        byte[] expected = {0x03, 0x02};
        assertArrayEquals(expected, actual);
    }
}
