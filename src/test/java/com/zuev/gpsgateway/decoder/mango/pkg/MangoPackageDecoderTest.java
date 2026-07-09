package com.zuev.gpsgateway.decoder.mango.pkg;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MangoPackageDecoderTest {
    private final TestMangoPackageDecoder decoder = new TestMangoPackageDecoder();

    @Test
    public void bodyShouldBeDecoded() {
        ByteBuf givenBody = wrappedBuffer(decodeHexDump("00090000002a68656c6c6f0000"));

        Object actual = decoder.decodeBody(givenBody);
        TestPackage expected = new TestPackage(42, "hello");
        assertEquals(expected, actual);
        assertEquals(0, givenBody.readableBytes());
    }

    private record TestPackage(int number, String message) {
    }

    private static final class TestMangoPackageDecoder extends MangoPackageDecoder {
        private static final byte[] PREFIX = {0x56, 0x5A, 0x09};

        public TestMangoPackageDecoder() {
            super(PREFIX);
        }

        @Override
        protected Object decodePayload(ByteBuf payload) {
            int number = payload.readInt();
            String message = payload.toString(UTF_8);
            return new TestPackage(number, message);
        }
    }
}
