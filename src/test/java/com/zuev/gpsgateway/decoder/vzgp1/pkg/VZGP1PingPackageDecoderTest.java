package com.zuev.gpsgateway.decoder.vzgp1.pkg;

import com.zuev.gpsgateway.model.vzgp1.VZGP1PingPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class VZGP1PingPackageDecoderTest {
    private final VZGP1PingPackageDecoder decoder = new VZGP1PingPackageDecoder();

    @Test
    public void payloadShouldBeDecoded() {
        ByteBuf givenByteBuf = mock(ByteBuf.class);

        VZGP1PingPackage actual = decoder.decodePayload(givenByteBuf);
        VZGP1PingPackage expected = new VZGP1PingPackage();
        assertEquals(expected, actual);

        verifyNoInteractions(givenByteBuf);
    }
}
