package com.zuev.gpsgateway.decoder.base.pkg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class PrefixedTextPackageDecoderTest {
    private final TestPrefixedTextPackageDecoder decoder = new TestPrefixedTextPackageDecoder();

    @Test
    public void sourceShouldStartWithPrefix() {
        String givenSource = "#TEST#155;43";
        String prefix = "#TEST#";

        assertTrue(decoder.startsWithPrefix(givenSource, prefix));
    }

    @Test
    public void sourceShouldNotStartWithPrefix() {
        String givenSource = "#TEST#155;43";
        String prefix = "#TEST2#";

        assertFalse(decoder.startsWithPrefix(givenSource, prefix));
    }

    @Test
    public void prefixLengthShouldBeGot() {
        String givenPrefix = "#TEST#";

        int actual = decoder.getLength(givenPrefix);
        int expected = 6;
        assertEquals(expected, actual);
    }

    @Test
    public void sourceShouldBeSkipped() {
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
