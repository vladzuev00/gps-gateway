package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoLoginPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MangoLoginPackageDecoderTest {
    private final MangoLoginPackageDecoder decoder = new MangoLoginPackageDecoder();

    @Test
    public void payloadShouldBeDecoded() {
        ByteBuf givenPayload = wrappedBuffer(decodeHexDump("3535353535353535353535353535350474657374"));

        MangoLoginPackage actual = decoder.decodePayload(givenPayload);
        MangoLoginPackage expected = new MangoLoginPackage("555555555555555", "test");
        assertEquals(expected, actual);
        assertEquals(0, givenPayload.readableBytes());
    }
}
