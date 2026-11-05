package com.zuev.gpsgateway.encoder.base.pkg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class PackageEncoderTest {
    private final TestPackageEncoder encoder = new TestPackageEncoder();

    @Test
    public void encoderShouldBeAbleToEncodePackage() {
        TestPackage givenPackage = new TestPackage((byte) 5);

        assertTrue(encoder.isAbleEncode(givenPackage));
    }

    @Test
    public void encoderShouldNotBeAbleToEncodePackage() {
        Object givenPackage = new Object();

        assertFalse(encoder.isAbleEncode(givenPackage));
    }

    @Test
    public void packageShouldBeEncoded() {
        TestPackage givenPackage = new TestPackage((byte) 5);

        byte[] actual = encoder.encode(givenPackage);
        byte[] expected = {5};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void packageShouldNotBeEncodedBecauseOfNotSuitablePackageType() {
        Object givenPackage = new Object();

        assertThrows(ClassCastException.class, () -> encoder.encode(givenPackage));
    }

    private record TestPackage(byte value) {
    }

    private static final class TestPackageEncoder extends PackageEncoder<TestPackage> {

        public TestPackageEncoder() {
            super(TestPackage.class);
        }

        @Override
        protected byte[] encodeInternal(TestPackage pkg) {
            return new byte[]{pkg.value()};
        }
    }
}
