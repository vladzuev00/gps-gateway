package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoPingResponse;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class MangoPingResponseEncoderTest {
    private final MangoPingResponseEncoder encoder = new MangoPingResponseEncoder();

    @Test
    public void payloadShouldBePut() {
        ByteBuffer givenBuffer = ByteBuffer.allocate(0);
        MangoPingResponse givenPackage = mock(MangoPingResponse.class);

        encoder.putPayload(givenBuffer, givenPackage);

        assertEquals(0, givenBuffer.position());
        verifyNoInteractions(givenPackage);
    }
}
