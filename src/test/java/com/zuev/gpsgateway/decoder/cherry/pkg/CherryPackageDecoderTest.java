package com.zuev.gpsgateway.decoder.cherry.pkg;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CherryPackageDecoderTest {
    private final CherryPackageDecoder decoder = new TestCherryPackageDecoder();

    @ParameterizedTest
    @MethodSource("bodyShouldBeDecodedMethodSource")
    public void bodyShouldBeDecoded(String givenBody, List<String> expectedFields) {
        Object actual = decoder.decodeBody(givenBody);
        TestPackage expected = new TestPackage(expectedFields);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> bodyShouldBeDecodedMethodSource() {
        return Stream.of(
                Arguments.of(";second;;fourth;;;;1234\0", List.of("", "second", "", "fourth", "", "", "")),
                Arguments.of("430\0", emptyList())
        );
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
