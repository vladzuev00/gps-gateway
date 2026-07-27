package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.*;

public final class PrefixedBinaryPackageDecoderTest {
    private final TestPrefixedBinaryPackageDecoder decoder = new TestPrefixedBinaryPackageDecoder();

    @Test
    public void sourceShouldStartWithPrefix() {
        ByteBuf givenSource = wrappedBuffer(decodeHexDump("565a0100143535353535353535"));
        byte[] givenPrefix = {0x56, 0x5a, 0x01};

        assertTrue(decoder.startsWithPrefix(givenSource, givenPrefix));
        assertEquals(0, givenSource.readerIndex());
    }

    @Test
    public void sourceShouldNotStartWithPrefix() {
        ByteBuf givenSource = wrappedBuffer(decodeHexDump("565a0100143535353535353535"));
        byte[] givenPrefix = {0x56, 0x5a, 0x02};

        assertFalse(decoder.startsWithPrefix(givenSource, givenPrefix));
        assertEquals(0, givenSource.readerIndex());
    }

    @Test
    public void prefixLengthShouldBeGot() {
        byte[] givenPrefix = {0x56, 0x5a, 0x01};

        int actual = decoder.getLength(givenPrefix);
        int expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    public void sourceShouldBeSkipped() {
        ByteBuf givenSource = wrappedBuffer(decodeHexDump("565a0100143535353535353535"));
        int givenLength = 3;

        ByteBuf actual = decoder.skip(givenSource, givenLength);
        assertSame(givenSource, actual);
        assertEquals(3, actual.readerIndex());
    }

    private static final class TestPrefixedBinaryPackageDecoder extends PrefixedBinaryPackageDecoder {

        public TestPrefixedBinaryPackageDecoder() {
            super(new byte[]{});
        }

        @Override
        protected Object decodeBody(ByteBuf body) {
            throw new UnsupportedOperationException();
        }
    }
}
