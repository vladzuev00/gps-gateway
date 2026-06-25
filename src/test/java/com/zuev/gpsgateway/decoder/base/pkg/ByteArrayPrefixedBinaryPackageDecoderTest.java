package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.*;

public final class ByteArrayPrefixedBinaryPackageDecoderTest {
    private final TestByteArrayPrefixedBinaryPackageDecoder decoder = new TestByteArrayPrefixedBinaryPackageDecoder();

    @Test
    public void prefixShouldBeGot() {
        ByteBuf givenSource = wrappedBuffer(decodeHexDump("565a0100143535353535353535"));
        int givenLength = 3;

        byte[] actual = decoder.getPrefix(givenSource, givenLength);
        byte[] expected = {0x56, 0x5A, 0x01};
        assertArrayEquals(expected, actual);
        assertEquals(0, givenSource.readerIndex());
    }

    @Test
    public void prefixLengthShouldBeGot() {
        byte[] givenPrefix = {0x56, 0x5A, 0x01};

        int actual = decoder.getLength(givenPrefix);
        int expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    public void prefixesShouldBeEqual() {
        byte[] givenFirstPrefix = {0x56, 0x5A, 0x01};
        byte[] givenSecondPrefix = {0x56, 0x5A, 0x01};

        assertTrue(decoder.equals(givenFirstPrefix, givenSecondPrefix));
    }

    @Test
    public void prefixesShouldNotBeEqual() {
        byte[] givenFirstPrefix = {0x56, 0x5A, 0x01};
        byte[] givenSecondPrefix = {0x56, 0x5A, 0x02};

        assertFalse(decoder.equals(givenFirstPrefix, givenSecondPrefix));
    }

    private static final class TestByteArrayPrefixedBinaryPackageDecoder extends ByteArrayPrefixedBinaryPackageDecoder {

        public TestByteArrayPrefixedBinaryPackageDecoder() {
            super(new byte[]{});
        }

        @Override
        protected Object decodeBody(ByteBuf body) {
            throw new UnsupportedOperationException();
        }
    }
}
