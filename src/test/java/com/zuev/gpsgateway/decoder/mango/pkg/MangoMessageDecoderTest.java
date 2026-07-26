package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoMessage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MangoMessageDecoderTest {
    private final MangoMessageDecoder decoder = new MangoMessageDecoder();

    @Test
    public void messageWithOptionalFieldsShouldBeDecode() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("0000018bcfe56800404a0fcd67fd3f5b40107e6b3fe9fadb7f003c00b441280000083f99999a01550000018bcfe56be8404a53c0ca600b0340129e065300581500"));

        MangoMessage actual = decoder.decode(givenByteBuf);
        MangoMessage expected = new MangoMessage(
                1700000000000L,
                52.123456,
                4.123456,
                (short) 60,
                (short) 180,
                10.5F,
                (byte) 8,
                1.2F,
                (byte) 1,
                (byte) 85
        );
        assertEquals(expected, actual);
        assertEquals(40, givenByteBuf.readerIndex());
    }

    @Test
    public void messageWithoutOptionalFieldsShouldBeDecode() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("0000018bcfe56800404a0fcd67fd3f5b40107e6b3fe9fadb000000018bcfe56be8404a53c0ca600b0340129e06530058157f003c00b441280000083f99999a0155"));

        MangoMessage actual = decoder.decode(givenByteBuf);
        MangoMessage expected = new MangoMessage(
                1700000000000L,
                52.123456,
                4.123456,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertEquals(expected, actual);
        assertEquals(25, givenByteBuf.readerIndex());
    }
}
