package com.zuev.gpsgateway.encoder.lime.pkg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class LimeResponseEncoderTest {
    private final TestLimeResponseEncoder encoder = new TestLimeResponseEncoder();

    @Test
    public void packageShouldBeEncodedInternally() {
        TestPackage givenPackage = new TestPackage(new byte[]{0x01, 0x02});

        byte[] actual = encoder.encodeInternal(givenPackage);
        byte[] expected = {0x02, 0x00, 0x01, 0x02};
        assertArrayEquals(expected, actual);
    }

    private record TestPackage(byte[] tagValuePairs) {
    }

    private static final class TestLimeResponseEncoder extends LimeResponseEncoder<TestPackage> {

        public TestLimeResponseEncoder() {
            super(TestPackage.class);
        }

        @Override
        protected byte[] encodeTagValuePairs(TestPackage pkg) {
            return pkg.tagValuePairs();
        }
    }
}
