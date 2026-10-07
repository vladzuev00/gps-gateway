package com.zuev.gpsgateway.decoder.cherry.pkg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class CherryPackageDecoderTest {
    private final TestCherryPackageDecoder decoder = new TestCherryPackageDecoder();

    @Test
    public void bodyShouldBeDecoded() {
        String givenBody = "first;;third;1234\0";

        String[] actual = (String[]) decoder.decodeBody(givenBody);
        String[] expected = {"first", "", "third"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void bodyWithEmptyLastFieldShouldBeDecoded() {
        String givenBody = "first;;1234\0";

        String[] actual = (String[]) decoder.decodeBody(givenBody);
        String[] expected = {"first", ""};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void bodyWithSeveralEmptyLastFieldsShouldBeDecoded() {
        String givenBody = "a;;b;;;1234\0";

        String[] actual = (String[]) decoder.decodeBody(givenBody);
        String[] expected = {"a", "", "b", "", ""};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void bodyWithoutFieldsShouldBeDecoded() {
        String givenBody = "\0";

        String[] actual = (String[]) decoder.decodeBody(givenBody);
        String[] expected = {};
        assertArrayEquals(expected, actual);
    }

    private static final class TestCherryPackageDecoder extends CherryPackageDecoder {
        private static final String PREFIX = "@TEST@";

        public TestCherryPackageDecoder() {
            super(PREFIX);
        }

        @Override
        protected String[] decodeFields(String[] fields) {
            return fields;
        }
    }
}
