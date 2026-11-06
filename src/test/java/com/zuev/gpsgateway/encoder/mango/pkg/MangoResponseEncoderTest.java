package com.zuev.gpsgateway.encoder.mango.pkg;

import org.junit.jupiter.api.Test;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class MangoResponseEncoderTest {
    private final TestMangoResponseEncoder encoder = new TestMangoResponseEncoder();

    @Test
    public void packageShouldBeEncodedInternally() {
        TestPackage givenPackage = new TestPackage("test message");

        byte[] actual = encoder.encodeInternal(givenPackage);
        byte[] expected = {0x56, 0x5A, 0x09, 0x00, 0x0C, 't', 'e', 's', 't', ' ', 'm', 'e', 's', 's', 'a', 'g', 'e'};
        assertArrayEquals(expected, actual);
    }

    private record TestPackage(String message) {
    }

    private static final class TestMangoResponseEncoder extends MangoResponseEncoder<TestPackage> {
        private static final byte[] PREFIX = {0x56, 0x5A, 0x09};

        public TestMangoResponseEncoder() {
            super(TestPackage.class, PREFIX);
        }

        @Override
        protected byte[] encodePayload(TestPackage pkg) {
            return pkg.message().getBytes(US_ASCII);
        }
    }
}
