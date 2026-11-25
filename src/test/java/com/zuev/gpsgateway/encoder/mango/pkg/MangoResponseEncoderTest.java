package com.zuev.gpsgateway.encoder.mango.pkg;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class MangoResponseEncoderTest {
    private final TestMangoResponseEncoder encoder = new TestMangoResponseEncoder();

    @Test
    public void packageShouldBeEncodedInternally() {
        TestPackage givenPackage = new TestPackage(0x01020304);

        byte[] actual = encoder.encodeInternal(givenPackage);
        byte[] expected = {0x56, 0x5A, 0x09, 0x00, 0x04, 0x01, 0x02, 0x03, 0x04};
        assertArrayEquals(expected, actual);
    }

    private record TestPackage(int value) {
    }

    private static final class TestMangoResponseEncoder extends MangoResponseEncoder<TestPackage> {
        private static final byte TYPE = 0x09;

        public TestMangoResponseEncoder() {
            super(TestPackage.class, TYPE, Integer.BYTES);
        }

        @Override
        protected void putPayload(ByteBuffer buffer, TestPackage pkg) {
            buffer.putInt(pkg.value());
        }
    }
}
