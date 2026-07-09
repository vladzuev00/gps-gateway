package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoPingPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class MangoPingPackageDecoderTest {
    private final MangoPingPackageDecoder decoder = new MangoPingPackageDecoder();

    @Test
    public void payloadShouldBeDecoded() {
        ByteBuf givenPayload = mock(ByteBuf.class);

        MangoPingPackage actual = decoder.decodePayload(givenPayload);
        MangoPingPackage expected = new MangoPingPackage();
        assertEquals(expected, actual);

        verifyNoInteractions(givenPayload);
    }
}
