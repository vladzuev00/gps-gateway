package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeLoginResponse;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class LimeLoginResponseEncoderTest {
    private final LimeLoginResponseEncoder encoder = new LimeLoginResponseEncoder();

    @Test
    public void tagValuePairsShouldBePut() {
        ByteBuffer givenBuffer = ByteBuffer.allocate(Byte.BYTES + Byte.BYTES);
        LimeLoginResponse givenPackage = new LimeLoginResponse((byte) 0x02);

        encoder.putTagValuePairs(givenBuffer, givenPackage);

        byte[] actual = givenBuffer.array();
        byte[] expected = {0x03, 0x02};
        assertArrayEquals(expected, actual);
    }
}
