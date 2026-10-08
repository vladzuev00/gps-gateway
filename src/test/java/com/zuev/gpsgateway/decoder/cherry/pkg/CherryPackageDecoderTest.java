package com.zuev.gpsgateway.decoder.cherry.pkg;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CherryPackageDecoderTest {
    private final CherryPackageDecoder decoder = new TestCherryPackageDecoder();

    @Test
    public void bodyShouldBeDecoded() {
        String givenBody = "first;;third;;;;1234\0";

        Object actual = decoder.decodeBody(givenBody);
        TestPackage expected = new TestPackage(List.of("first", "", "third", "", "", ""));
        assertEquals(expected, actual);
    }

    @Test
    public void bodyWithoutFieldsShouldBeDecoded() {
        String givenBody = "\0";

        Object actual = decoder.decodeBody(givenBody);
        TestPackage expected = new TestPackage(emptyList());
        assertEquals(expected, actual);
    }

    private record TestPackage(List<String> fields) {
    }

    private static final class TestCherryPackageDecoder extends CherryPackageDecoder {
        private static final String PREFIX = "@TEST@";

        public TestCherryPackageDecoder() {
            super(PREFIX);
        }

        @Override
        protected TestPackage decodeFields(Iterator<String> iterator) {
            List<String> fields = new ArrayList<>();
            iterator.forEachRemaining(fields::add);
            return new TestPackage(fields);
        }
    }
}
