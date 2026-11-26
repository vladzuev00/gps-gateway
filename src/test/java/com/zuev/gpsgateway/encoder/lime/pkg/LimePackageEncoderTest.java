package com.zuev.gpsgateway.encoder.lime.pkg;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class LimePackageEncoderTest {
    private final TestLimePackageEncoder encoder = new TestLimePackageEncoder();

    @Test
    public void packageShouldBeEncodedInternally() {
        TestPackage givenPackage = new TestPackage(0x0201);

        byte[] actual = encoder.encodeInternal(givenPackage);
        byte[] expected = {0x05, 0x00, 0x0C, 0x01, 0x02, 0x00, 0x00};
        assertArrayEquals(expected, actual);
    }

    private record TestPackage(int number) {
    }

    private static final class TestLimePackageEncoder extends LimePackageEncoder<TestPackage> {
        private static final byte NUMBER_TAG = 0x0C;
        private static final int TAG_VALUE_PAIRS_LENGTH = 1 + Integer.BYTES;

        public TestLimePackageEncoder() {
            super(TestPackage.class, TAG_VALUE_PAIRS_LENGTH);
        }

        @Override
        protected void putTagValuePairs(ByteBuffer buffer, TestPackage pkg) {
            buffer.put(NUMBER_TAG);
            buffer.putInt(pkg.number());
        }
    }
}
