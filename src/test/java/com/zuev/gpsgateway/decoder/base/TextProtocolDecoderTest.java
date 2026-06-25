package com.zuev.gpsgateway.decoder.base;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.util.OptionalInt;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class TextProtocolDecoderTest {
    private final TestTextProtocolDecoder decoder = new TestTextProtocolDecoder();

    @Test
    public void packageSourceShouldBeCreated() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("746573742074657874"));

        String actual = decoder.toPackageSource(givenByteBuf);
        String expected = "test text";
        assertEquals(expected, actual);
    }

    @Test
    public void logStringShouldBeCreated() {
        String givenPackageSource = "test text";

        String actual = decoder.toLogString(givenPackageSource);
        assertSame(givenPackageSource, actual);
    }

    private static final class TestTextProtocolDecoder extends TextProtocolDecoder {

        public TestTextProtocolDecoder() {
            super(emptyList(), UTF_8);
        }

        @Override
        protected OptionalInt findCompletePackageEnd(ByteBuf byteBuf) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected OptionalInt getChecksum(ByteBuf byteBuf) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected int calculateChecksum(ByteBuf byteBuf) {
            throw new UnsupportedOperationException();
        }
    }
}
