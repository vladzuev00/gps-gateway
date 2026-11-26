package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeSetDataIntervalCommand;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class LimeSetDataIntervalCommandEncoderTest {
    private final LimeSetDataIntervalCommandEncoder encoder = new LimeSetDataIntervalCommandEncoder();

    @Test
    public void tagValuePairsShouldBePut() {
        ByteBuffer givenBuffer = ByteBuffer.allocate(1 + Short.BYTES);
        LimeSetDataIntervalCommand givenPackage = new LimeSetDataIntervalCommand(65_535);

        encoder.putTagValuePairs(givenBuffer, givenPackage);

        byte[] actual = givenBuffer.array();
        byte[] expected = {0x0C, (byte) 0xFF, (byte) 0xFF};
        assertArrayEquals(expected, actual);
    }
}
