package com.zuev.gpsgateway.handler.lime.pkg;

import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.model.lime.LimeDataPackage;
import com.zuev.gpsgateway.model.lime.LimeDataResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.zuev.gpsgateway.model.MessageAdditionalParameterNames.BATTERY;
import static java.lang.System.currentTimeMillis;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class LimeDataPackageHandlerTest {
    private final LimeDataPackageHandler handler = new LimeDataPackageHandler(null);

    @Test
    public void messageSourcesShouldBeGot() {
        LimeDataPackage givenPackage = mock(LimeDataPackage.class);

        List<LimeDataPackage> actual = handler.getMessageSources(givenPackage);
        List<LimeDataPackage> expected = List.of(givenPackage);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("sourceShouldBeMappedToMessageMethodSource")
    public void sourceShouldBeMappedToMessage(LimeDataPackage givenSource, long givenTrackerId, Message expected) {
        Message actual = handler.toMessage(givenSource, givenTrackerId);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("epochMillis")
                .isEqualTo(expected);
        assertThat(actual.epochMillis()).isCloseTo(currentTimeMillis(), within(1000L));
    }

    @Test
    public void responseShouldBeCreated() {
        LimeDataPackage givenPackage = mock(LimeDataPackage.class);

        LimeDataResponse actual = handler.createResponse(givenPackage);
        LimeDataResponse expected = new LimeDataResponse();
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage);
    }

    private static Stream<Arguments> sourceShouldBeMappedToMessageMethodSource() {
        return Stream.of(
                Arguments.of(
                        new LimeDataPackage(
                                55.75,
                                37.62,
                                (short) 60,
                                (short) 180,
                                150.5F,
                                (byte) 8,
                                1.2F,
                                (byte) 95
                        ),
                        255,
                        new Message(
                                37.62,
                                55.75,
                                0,
                                60.,
                                180.,
                                150.5,
                                8,
                                1.2,
                                Map.of(BATTERY, "95"),
                                255
                        )
                ),
                Arguments.of(
                        new LimeDataPackage(
                                55.75,
                                37.62,
                                (short) 60,
                                (short) 180,
                                150.5F,
                                (byte) 8,
                                null,
                                null
                        ),
                        255,
                        new Message(
                                37.62,
                                55.75,
                                0,
                                60.,
                                180.,
                                150.5,
                                8,
                                null,
                                emptyMap(),
                                255
                        )
                )
        );
    }
}
