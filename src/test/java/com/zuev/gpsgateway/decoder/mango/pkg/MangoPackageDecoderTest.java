package com.zuev.gpsgateway.decoder.mango.pkg;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.getField;

public final class MangoPackageDecoderTest {
    private final MangoPackageDecoder decoder = new TestMangoPackageDecoder();

    @Test
    public void decoderShouldBeCreated() {
        byte[] actual = getDecoderPrefix();
        byte[] expected = {0x56, 0x5A, 0x09};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void bodyShouldBeDecoded() {
        ByteBuf givenBody = wrappedBuffer(decodeHexDump("00090000002a68656c6c6f0000"));

        Object actual = decoder.decodeBody(givenBody);
        TestPackage expected = new TestPackage(42, "hello");
        assertEquals(expected, actual);
        assertFalse(givenBody.isReadable());
    }

    private byte[] getDecoderPrefix() {
        return (byte[]) getField(decoder, "prefix");
    }

    private record TestPackage(int number, String message) {
    }

    private static final class TestMangoPackageDecoder extends MangoPackageDecoder {
        private static final byte TYPE = 0x09;

        public TestMangoPackageDecoder() {
            super(TYPE);
        }

        @Override
        protected Object decodePayload(ByteBuf payload) {
            int number = payload.readInt();
            String message = payload.toString(US_ASCII);
            return new TestPackage(number, message);
        }
    }
}
