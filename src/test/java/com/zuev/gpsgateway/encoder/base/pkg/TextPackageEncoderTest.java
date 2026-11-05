package com.zuev.gpsgateway.encoder.base.pkg;

import org.junit.jupiter.api.Test;

import static java.nio.charset.StandardCharsets.UTF_16BE;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class TextPackageEncoderTest {
    private final TestPackageEncoder encoder = new TestPackageEncoder();

    @Test
    public void packageShouldBeEncoded() {
        TestPackage givenPackage = new TestPackage("AB");

        byte[] actual = encoder.encode(givenPackage);
        byte[] expected = {0, 'A', 0, 'B'};
        assertArrayEquals(expected, actual);
    }

    private record TestPackage(String value) {
    }

    private static final class TestPackageEncoder extends TextPackageEncoder<TestPackage> {

        public TestPackageEncoder() {
            super(TestPackage.class, UTF_16BE);
        }

        @Override
        protected String encodeToString(TestPackage pkg) {
            return pkg.value();
        }
    }
}
