package com.zuev.gpsgateway.encoder.mango.pkg;

import org.junit.jupiter.api.Test;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class MangoResponsePackageEncoderTest {
    private final TestMangoResponsePackageEncoder encoder = new TestMangoResponsePackageEncoder();

    @Test
    public void packageShouldBeEncoded() {
        TestPackage givenPackage = new TestPackage("test message");

        byte[] actual = encoder.encode(givenPackage);
        byte[] expected = {0x56, 0x5A, 0x09, 0x00, 0x0C, 't', 'e', 's', 't', ' ', 'm', 'e', 's', 's', 'a', 'g', 'e'};
        assertArrayEquals(expected, actual);
    }

    private record TestPackage(String message) {
    }

    private static final class TestMangoResponsePackageEncoder extends MangoResponsePackageEncoder<TestPackage> {
        private static final byte[] PREFIX = {0x56, 0x5A, 0x09};

        public TestMangoResponsePackageEncoder() {
            super(TestPackage.class, PREFIX);
        }

        @Override
        protected byte[] encodePayload(TestPackage pkg) {
            return pkg.message().getBytes(US_ASCII);
        }
    }
}
