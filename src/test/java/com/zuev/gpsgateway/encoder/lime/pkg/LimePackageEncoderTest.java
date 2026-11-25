package com.zuev.gpsgateway.encoder.lime.pkg;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class LimePackageEncoderTest {
    private final TestLimePackageEncoder encoder = new TestLimePackageEncoder();

    @Test
    public void packageShouldBeEncodedInternally() {
        TestPackage givenPackage = new TestPackage(new byte[]{0x01, 0x02});

        byte[] actual = encoder.encodeInternal(givenPackage);
        byte[] expected = {0x02, 0x00, 0x01, 0x02};
        assertArrayEquals(expected, actual);
    }

    private record TestPackage(byte[] encodedTagValuePairs) {
    }

    private static final class TestLimePackageEncoder extends LimePackageEncoder<TestPackage> {

        public TestLimePackageEncoder() {
            super(TestPackage.class);
        }

        @Override
        protected int getTagValuePairsLength(TestPackage pkg) {
            return pkg.encodedTagValuePairs().length;
        }

        @Override
        protected void putTagValuePairs(ByteBuffer buffer, TestPackage pkg) {
            buffer.put(pkg.encodedTagValuePairs());
        }
    }
}
