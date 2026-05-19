package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class PrefixedBinaryPackageDecoderTest {
    private final TestPrefixedBinaryPackageDecoder decoder = new TestPrefixedBinaryPackageDecoder();

    @Test
    public void prefixShouldBeGot() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("2354455354232323"));
        int givenLength = 6;

        String actual = decoder.getPrefix(givenByteBuf, givenLength);
        String expected = "#TEST#";
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readerIndex());
    }

    @Test
    public void byteBufShouldBeSkipped() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("2354455354232323"));
        int givenLength = 6;

        ByteBuf actual = decoder.skip(givenByteBuf, givenLength);
        assertSame(givenByteBuf, actual);
        assertEquals(6, actual.readerIndex());
    }

    private static final class TestPrefixedBinaryPackageDecoder extends PrefixedBinaryPackageDecoder<String> {
        private static final String PREFIX = "#TEST#";

        public TestPrefixedBinaryPackageDecoder() {
            super(PREFIX);
        }

        @Override
        protected int getLength(String prefix) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected String readPrefix(ByteBuf byteBuf, int length) {
            return byteBuf.readString(length, UTF_8);
        }

        @Override
        protected boolean equals(String first, String second) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Object decodeBody(ByteBuf byteBuf) {
            throw new UnsupportedOperationException();
        }
    }
}
