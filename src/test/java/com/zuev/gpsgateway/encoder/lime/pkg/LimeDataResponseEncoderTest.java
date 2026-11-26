package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeDataResponse;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class LimeDataResponseEncoderTest {
    private final LimeDataResponseEncoder encoder = new LimeDataResponseEncoder();

    @Test
    public void tagValuePairsShouldBePut() {
        ByteBuffer givenBuffer = ByteBuffer.allocate(0);
        LimeDataResponse givenPackage = mock(LimeDataResponse.class);

        encoder.putTagValuePairs(givenBuffer, givenPackage);

        assertEquals(0, givenBuffer.position());
        verifyNoInteractions(givenPackage);
    }
}
