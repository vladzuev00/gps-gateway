package com.zuev.gpsgateway.handler.cherry.pkg;

import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.model.cherry.CherryMessage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.zuev.gpsgateway.model.MessageParameterNames.IGNITION;
import static com.zuev.gpsgateway.model.MessageParameterNames.ODOMETER;
import static java.time.Instant.parse;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CherryMessagePackageHandlerTest {
    private final TestCherryMessagePackageHandler handler = new TestCherryMessagePackageHandler();

    @ParameterizedTest
    @MethodSource("sourceShouldBeMappedToMessageMethodSource")
    public void sourceShouldBeMappedToMessage(CherryMessage givenSource, long givenTrackerId, Message expected) {
        Message actual = handler.toMessage(givenSource, givenTrackerId);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> sourceShouldBeMappedToMessageMethodSource() {
        return Stream.of(
                Arguments.of(
                        new CherryMessage(
                                parse("2023-11-14T22:13:20Z"),
                                55.75,
                                37.62,
                                (short) 60,
                                (short) 180,
                                150.5F,
                                (byte) 8,
                                1.2F,
                                (byte) 1,
                                125430
                        ),
                        255,
                        new Message(
                                37.62,
                                55.75,
                                1700000000000L,
                                60.,
                                180.,
                                150.5,
                                8,
                                1.2,
                                Map.of(IGNITION, "1", ODOMETER, "125430"),
                                255
                        )
                ),
                Arguments.of(
                        new CherryMessage(
                                parse("2023-11-14T22:13:20Z"),
                                55.75,
                                37.62,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                        ),
                        255,
                        new Message(
                                37.62,
                                55.75,
                                1700000000000L,
                                null,
                                null,
                                null,
                                null,
                                null,
                                emptyMap(),
                                255
                        )
                )
        );
    }

    private static final class TestCherryMessagePackageHandler extends CherryMessagePackageHandler<Object> {

        public TestCherryMessagePackageHandler() {
            super(Object.class, null);
        }

        @Override
        protected List<CherryMessage> getMessageSources(Object pkg) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Object createResponse(Object pkg) {
            throw new UnsupportedOperationException();
        }
    }
}
