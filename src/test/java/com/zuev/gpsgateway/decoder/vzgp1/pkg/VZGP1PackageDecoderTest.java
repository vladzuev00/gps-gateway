package com.zuev.gpsgateway.decoder.vzgp1.pkg;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class VZGP1PackageDecoderTest {
    private final TestVZGP1PackageDecoder decoder = new TestVZGP1PackageDecoder();

    @Test
    public void bodyShouldBeDecoded() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("00090000002a68656c6c6f0000"));

        Object actual = decoder.decodeBody(givenByteBuf);
        TestPackage expected = new TestPackage(42, "hello");
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readableBytes());
    }

    private record TestPackage(int number, String message) {
    }

    private static final class TestVZGP1PackageDecoder extends VZGP1PackageDecoder {
        private static final byte[] PREFIX = {0x56, 0x5A, 0x09};

        public TestVZGP1PackageDecoder() {
            super(PREFIX);
        }

        @Override
        protected Object decodePayload(ByteBuf byteBuf) {
            int number = byteBuf.readInt();
            String message = byteBuf.toString(UTF_8);
            return new TestPackage(number, message);
        }
    }
}
