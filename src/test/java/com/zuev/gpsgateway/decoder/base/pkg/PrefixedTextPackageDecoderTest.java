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
        String givenSource = "#TEST#155;43";
        int givenLength = 6;

        String actual = decoder.getPrefix(givenSource, givenLength);
        String expected = "#TEST#";
        assertEquals(expected, actual);
    }

    @Test
    public void prefixesShouldBeEqual() {
        String givenFirstPrefix = "#FIRST#";
        String givenSecondPrefix = "#FIRST#";

        assertTrue(decoder.equals(givenFirstPrefix, givenSecondPrefix));
    }

    @Test
    public void prefixesShouldNotBeEqual() {
        String givenFirstPrefix = "#FIRST#";
        String givenSecondPrefix = "#SECOND#";

        assertFalse(decoder.equals(givenFirstPrefix, givenSecondPrefix));
    }

    @Test
    public void charsShouldBeSkipped() {
        String givenSource = "#TEST#155;43";
        int givenLength = 6;

        String actual = decoder.skip(givenSource, givenLength);
        String expected = "155;43";
        assertEquals(expected, actual);
    }

    private static final class TestPrefixedTextPackageDecoder extends PrefixedTextPackageDecoder {
        private static final String PREFIX = "#TEST#";

        public TestPrefixedTextPackageDecoder() {
            super(PREFIX);
        }

        @Override
        protected Object decodeBody(String body) {
            throw new UnsupportedOperationException();
        }
    }
}
