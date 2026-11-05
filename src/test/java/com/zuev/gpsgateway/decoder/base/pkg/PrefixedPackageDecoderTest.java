package com.zuev.gpsgateway.decoder.base.pkg;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

public final class PrefixedPackageDecoderTest {
    private final TestPrefixedPackageDecoder decoder = new TestPrefixedPackageDecoder();

    @Test
    public void decoderShouldBeAbleToDecodeSource() {
        ByteBuf givenSource = wrappedBuffer(decodeHexDump("090000000574657374206d657373616765"));

        assertTrue(decoder.isAbleDecode(givenSource));
        assertEquals(0, givenSource.readerIndex());
    }

    @Test
    public void decoderShouldNotBeAbleToDecodeSource() {
        ByteBuf givenSource = wrappedBuffer(decodeHexDump("080000000574657374206d657373616765"));

        assertFalse(decoder.isAbleDecode(givenSource));
        assertEquals(0, givenSource.readerIndex());
    }

    @Test
    public void decoderShouldDecodeSource() {
        ByteBuf givenSource = wrappedBuffer(decodeHexDump("090000000574657374206d657373616765"));

        Object actual = decoder.decode(givenSource);
        TestPackage expected = new TestPackage(5, "test message");
        assertEquals(expected, actual);
    }

    private record TestPackage(int number, String message) {
    }

    private static final class TestPrefixedPackageDecoder extends PrefixedPackageDecoder<ByteBuf, Byte> {
        private static final Byte PREFIX = 9;

        public TestPrefixedPackageDecoder() {
            super(PREFIX);
        }

        @Override
        protected boolean startsWithPrefix(ByteBuf source, Byte prefix) {
            return source.getByte(source.readerIndex()) == prefix;
        }

        @Override
        protected int getLength(Byte prefix) {
            return Byte.BYTES;
        }

        @Override
        protected ByteBuf skip(ByteBuf source, int length) {
            return source.skipBytes(length);
        }

        @Override
        protected Object decodeBody(ByteBuf body) {
            int number = body.readInt();
            String message = body.toString(UTF_8);
            return new TestPackage(number, message);
        }
    }
}
