package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public final class ByteArrayPrefixedBinaryPackageDecoderTest {
    private final TestByteArrayPrefixedBinaryPackageDecoder decoder = new TestByteArrayPrefixedBinaryPackageDecoder();

    @Test
    public void prefixShouldBeRead() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("565a0100143535353535353535"));

        byte[] actual = decoder.readPrefix(givenByteBuf, 3);
        byte[] expected = {0x56, 0x5A, 0x01};
        assertArrayEquals(expected, actual);
        assertEquals(3, givenByteBuf.readerIndex());
    }

    @Test
    public void prefixLengthShouldBeGot() {
        byte[] givenBytes = {0x56, 0x5A, 0x01};

        int actual = decoder.getLength(givenBytes);
        int expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    public void prefixesShouldBeEqual() {
        byte[] givenFirst = {0x56, 0x5A, 0x01};
        byte[] givenSecond = {0x56, 0x5A, 0x01};

        assertTrue(decoder.equals(givenFirst, givenSecond));
    }

    @Test
    public void prefixesShouldNotBeEqual() {
        byte[] givenFirst = {0x56, 0x5A, 0x01};
        byte[] givenSecond = {0x56, 0x5A, 0x02};

        assertFalse(decoder.equals(givenFirst, givenSecond));
    }

    private static final class TestByteArrayPrefixedBinaryPackageDecoder extends ByteArrayPrefixedBinaryPackageDecoder {

        public TestByteArrayPrefixedBinaryPackageDecoder() {
            super(new byte[]{});
        }

        @Override
        protected Object decodeBody(ByteBuf byteBuf) {
            throw new UnsupportedOperationException();
        }
    }
}
