package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.EMPTY_BUFFER;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.*;

public final class ByteArrayPrefixedBinaryPackageDecoderTest {
    private final TestByteArrayPrefixedBinaryPackageDecoder decoder = new TestByteArrayPrefixedBinaryPackageDecoder();

    @Test
    public void sourceShouldStartWithPrefix() {
        ByteBuf givenSource = wrappedBuffer(decodeHexDump("565a0100143535353535353535"));
        ByteBuf givenPrefix = wrappedBuffer(decodeHexDump("565a01"));

        assertTrue(decoder.startsWithPrefix(givenSource, givenPrefix));
        assertEquals(0, givenSource.readerIndex());
        assertEquals(0, givenPrefix.readerIndex());
    }

    @Test
    public void sourceShouldNotStartWithPrefix() {
        ByteBuf givenSource = wrappedBuffer(decodeHexDump("565a0100143535353535353535"));
        ByteBuf givenPrefix = wrappedBuffer(decodeHexDump("565a02"));

        assertFalse(decoder.startsWithPrefix(givenSource, givenPrefix));
        assertEquals(0, givenSource.readerIndex());
        assertEquals(0, givenPrefix.readerIndex());
    }

    @Test
    public void prefixLengthShouldBeGot() {
        ByteBuf givenPrefix = wrappedBuffer(decodeHexDump("565a01"));

        int actual = decoder.getLength(givenPrefix);
        int expected = 3;
        assertEquals(expected, actual);
        assertEquals(0, givenPrefix.readerIndex());
    }

    @Test
    public void sourceShouldBeSkipped() {
        ByteBuf givenSource = wrappedBuffer(decodeHexDump("565a0100143535353535353535"));
        int givenLength = 3;

        ByteBuf actual = decoder.skip(givenSource, givenLength);
        assertSame(givenSource, actual);
        assertEquals(3, actual.readerIndex());
    }

    private static final class TestByteArrayPrefixedBinaryPackageDecoder extends ByteArrayPrefixedBinaryPackageDecoder {

        public TestByteArrayPrefixedBinaryPackageDecoder() {
            super(EMPTY_BUFFER);
        }

        @Override
        protected Object decodeBody(ByteBuf body) {
            throw new UnsupportedOperationException();
        }
    }
}
