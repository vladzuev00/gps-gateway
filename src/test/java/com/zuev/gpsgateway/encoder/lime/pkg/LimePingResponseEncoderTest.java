package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimePingResponse;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class LimePingResponseEncoderTest {
    private final LimePingResponseEncoder encoder = new LimePingResponseEncoder();

    @Test
    public void tagValuePairsShouldBePut() {
        ByteBuffer givenBuffer = ByteBuffer.allocate(0);
        LimePingResponse givenPackage = mock(LimePingResponse.class);

        encoder.putTagValuePairs(givenBuffer, givenPackage);

        assertEquals(0, givenBuffer.position());
        verifyNoInteractions(givenPackage);
    }
}
