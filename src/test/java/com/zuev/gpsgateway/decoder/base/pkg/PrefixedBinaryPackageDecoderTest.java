package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class PrefixedBinaryPackageDecoderTest {
    private final TestPrefixedBinaryPackageDecoder decoder = new TestPrefixedBinaryPackageDecoder();

    @Test
    public void byteBufShouldBeSkipped() {
        ByteBuf givenSource = wrappedBuffer(decodeHexDump("2354455354232323"));
        int givenLength = 6;

        ByteBuf actual = decoder.skip(givenSource, givenLength);
        assertSame(givenSource, actual);
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
        protected String getPrefix(ByteBuf source, int length) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected boolean equals(String firstPrefix, String secondPrefix) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Object decodeBody(ByteBuf body) {
            throw new UnsupportedOperationException();
        }
    }
}
