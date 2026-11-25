package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoDataResponse;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class MangoDataResponseEncoderTest {
    private final MangoDataResponseEncoder encoder = new MangoDataResponseEncoder();

    @Test
    public void payloadShouldBePut() {
        ByteBuffer givenBuffer = ByteBuffer.allocate(0);
        MangoDataResponse givenPackage = mock(MangoDataResponse.class);

        encoder.putPayload(givenBuffer, givenPackage);

        assertEquals(0, givenBuffer.position());
        verifyNoInteractions(givenPackage);
    }
}
