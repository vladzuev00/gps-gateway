package com.zuev.gpsgateway.decoder.base.pkg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class PrefixedTextPackageDecoderTest {
    private final TestPrefixedTextPackageDecoder decoder = new TestPrefixedTextPackageDecoder();

    @Test
    public void prefixLengthShouldBeGot() {
        String givenPrefix = "#TEST#";

        int actual = decoder.getLength(givenPrefix);
        int expected = 6;
        assertEquals(expected, actual);
    }

    @Test
    public void prefixShouldBeGot() {
        String givenText = "#TEST#155;43";
        int givenLength = 6;

        String actual = decoder.getPrefix(givenText, givenLength);
        String expected = "#TEST#";
        assertEquals(expected, actual);
    }

    @Test
    public void prefixesShouldBeEqual() {
        String givenFirst = "#FIRST#";
        String givenSecond = "#FIRST#";

        assertTrue(decoder.equals(givenFirst, givenSecond));
    }

    @Test
    public void prefixesShouldNotBeEqual() {
        String givenFirst = "#FIRST#";
        String givenSecond = "#SECOND#";

        assertFalse(decoder.equals(givenFirst, givenSecond));
    }

    @Test
    public void charsShouldBeSkipped() {
        String givenText = "#TEST#155;43";
        int givenLength = 6;

        String actual = decoder.skip(givenText, givenLength);
        String expected = "155;43";
        assertEquals(expected, actual);
    }

    private static final class TestPrefixedTextPackageDecoder extends PrefixedTextPackageDecoder {
        private static final String PREFIX = "#TEST#";

        public TestPrefixedTextPackageDecoder() {
            super(PREFIX);
        }

        @Override
        protected Object decodeBody(String text) {
            throw new UnsupportedOperationException();
        }
    }
}
