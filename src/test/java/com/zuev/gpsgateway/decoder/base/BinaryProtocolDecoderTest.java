package com.zuev.gpsgateway.decoder.base;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.util.OptionalInt;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

public final class BinaryProtocolDecoderTest {
    private final TestBinaryProtocolDecoder decoder = new TestBinaryProtocolDecoder();

    @Test
    public void packageSourceShouldBeCreated() {
        ByteBuf givenByteBuf = mock(ByteBuf.class);

        ByteBuf actual = decoder.toPackageSource(givenByteBuf);
        assertSame(givenByteBuf, actual);
    }

    @Test
    public void logStringShouldBeCreated() {
        ByteBuf givenByteBuf = wrappedBuffer(new byte[]{0x09, 0x00, 0x00, 0x00, 0x05, 0x74, 0x65, 0x73, 0x74, 0x20});

        String actual = decoder.toLogString(givenByteBuf);
        String expected = "09000000057465737420";
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readerIndex());
    }

    private static final class TestBinaryProtocolDecoder extends BinaryProtocolDecoder {

        public TestBinaryProtocolDecoder() {
            super(emptyList());
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
        protected int calculateChecksum(byte[] bytes) {
            throw new UnsupportedOperationException();
        }
    }
}
