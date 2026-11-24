package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoPingResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class MangoPingResponseEncoderTest {
    private final MangoPingResponseEncoder encoder = new MangoPingResponseEncoder();

    @Test
    public void payloadShouldBeEncoded() {
        MangoPingResponse givenPackage = mock(MangoPingResponse.class);

        byte[] actual = encoder.encodePayload(givenPackage);
        byte[] expected = {};
        assertArrayEquals(expected, actual);

        verifyNoInteractions(givenPackage);
    }
}
