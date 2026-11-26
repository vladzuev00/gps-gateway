package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeErrorResponse;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class LimeErrorResponseEncoderTest {
    private final LimeErrorResponseEncoder encoder = new LimeErrorResponseEncoder();

    @Test
    public void tagValuePairsShouldBePut() {
        ByteBuffer givenBuffer = ByteBuffer.allocate(Byte.BYTES + Byte.BYTES);
        LimeErrorResponse givenPackage = new LimeErrorResponse((byte) 0x03);

        encoder.putTagValuePairs(givenBuffer, givenPackage);

        byte[] actual = givenBuffer.array();
        byte[] expected = {0x0D, 0x03};
        assertArrayEquals(expected, actual);
    }
}
