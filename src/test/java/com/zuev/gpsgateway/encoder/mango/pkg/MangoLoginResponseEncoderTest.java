package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoLoginResponse;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class MangoLoginResponseEncoderTest {
    private final MangoLoginResponseEncoder encoder = new MangoLoginResponseEncoder();

    @Test
    public void payloadShouldBePut() {
        ByteBuffer givenBuffer = ByteBuffer.allocate(Byte.BYTES);
        MangoLoginResponse givenPackage = new MangoLoginResponse((byte) 0x02);

        encoder.putPayload(givenBuffer, givenPackage);

        byte[] actual = givenBuffer.array();
        byte[] expected = {0x02};
        assertArrayEquals(expected, actual);
    }
}
