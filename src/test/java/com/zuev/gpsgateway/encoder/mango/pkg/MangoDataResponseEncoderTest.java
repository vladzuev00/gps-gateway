package com.zuev.gpsgateway.encoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoDataResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class MangoDataResponseEncoderTest {
    private final MangoDataResponseEncoder encoder = new MangoDataResponseEncoder();

    @Test
    public void payloadShouldBeEncoded() {
        MangoDataResponse givenPackage = mock(MangoDataResponse.class);

        byte[] actual = encoder.encodePayload(givenPackage);
        byte[] expected = {};
        assertArrayEquals(expected, actual);

        verifyNoInteractions(givenPackage);
    }
}
