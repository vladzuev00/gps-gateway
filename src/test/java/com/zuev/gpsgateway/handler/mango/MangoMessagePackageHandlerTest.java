package com.zuev.gpsgateway.handler.mango;

import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.model.mango.MangoMessage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.zuev.gpsgateway.model.MessageParameterNames.BATTERY;
import static com.zuev.gpsgateway.model.MessageParameterNames.IGNITION;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MangoMessagePackageHandlerTest {
    private final TestMangoMessagePackageHandler handler = new TestMangoMessagePackageHandler();

    @ParameterizedTest
    @MethodSource("sourceShouldBeMappedToMessageMethodSource")
    public void sourceShouldBeMappedToMessage(MangoMessage givenSource, long givenTrackerId, Message expected) {
        Message actual = handler.toMessage(givenSource, givenTrackerId);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> sourceShouldBeMappedToMessageMethodSource() {
        return Stream.of(
                Arguments.of(
                        new MangoMessage(
                                1700000000000L,
                                55.75,
                                37.62,
                                (short) 60,
                                (short) 180,
                                150.5F,
                                (byte) 8,
                                1.2F,
                                (byte) 1,
                                (byte) 95
                        ),
                        255L,
                        new Message(
                                37.62,
                                55.75,
                                1700000000000L,
                                60.,
                                180.,
                                150.5,
                                8,
                                1.2,
                                Map.of(IGNITION, "1", BATTERY, "95"),
                                255
                        )
                ),
                Arguments.of(
                        new MangoMessage(
                                1700000000000L,
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
                        255L,
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

    private static final class TestMangoMessagePackageHandler extends MangoMessagePackageHandler<Object> {

        public TestMangoMessagePackageHandler() {
            super(Object.class, null);
        }

        @Override
        protected List<MangoMessage> getMessageSources(Object pkg) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Object createResponse(Object pkg) {
            throw new UnsupportedOperationException();
        }
    }
}
