package com.zuev.gpsgateway.decoder.vzgp1.pkg;

import com.zuev.gpsgateway.model.vzgp1.VZGP1AuthPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class VZGP1AuthPackageDecoderTest {
    private final VZGP1AuthPackageDecoder decoder = new VZGP1AuthPackageDecoder();

    @Test
    public void payloadShouldBeDecoded() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("3535353535353535353535353535350474657374"));

        VZGP1AuthPackage actual = decoder.decodePayload(givenByteBuf);
        VZGP1AuthPackage expected = new VZGP1AuthPackage("555555555555555", "test");
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readableBytes());
    }
}
