package com.zuev.gpsgateway.encoder.cherry.pkg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CherryResponseEncoderTest {
    private final TestCherryResponseEncoder encoder = new TestCherryResponseEncoder();

    @Test
    public void packageShouldBeEncodedToString() {
        TestPackage givenPackage = new TestPackage("test message");

        String actual = encoder.encodeToString(givenPackage);
        String expected = "@ATEST@test message\0";
        assertEquals(expected, actual);
    }

    private record TestPackage(String message) {
    }

    private static final class TestCherryResponseEncoder extends CherryResponseEncoder<TestPackage> {
        private static final String PREFIX = "@ATEST@";

        public TestCherryResponseEncoder() {
            super(TestPackage.class, PREFIX);
        }

        @Override
        protected String encodePayload(TestPackage pkg) {
            return pkg.message();
        }
    }
}
