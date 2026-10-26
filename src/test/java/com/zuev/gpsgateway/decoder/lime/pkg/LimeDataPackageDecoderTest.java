package com.zuev.gpsgateway.decoder.lime.pkg;

import com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder.ValueSlices;
import com.zuev.gpsgateway.model.lime.LimeDataPackage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder.Tags.*;
import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class LimeDataPackageDecoderTest {
    private final LimeDataPackageDecoder decoder = new LimeDataPackageDecoder();

    @ParameterizedTest
    @MethodSource("valueSlicesShouldBeDecodedMethodSource")
    public void valueSlicesShouldBeDecoded(@SuppressWarnings("ClassEscapesDefinedScope") ValueSlices givenSlices,
                                           LimeDataPackage expected) {
        LimeDataPackage actual = decoder.decodeValueSlices(givenSlices);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> valueSlicesShouldBeDecodedMethodSource() {
        return Stream.of(
                Arguments.of(
                        new ValueSlices(
                                Map.of(
                                        LATITUDE, wrappedBuffer(decodeHexDump("0000000000e04b40")),
                                        LONGITUDE, wrappedBuffer(decodeHexDump("8fc2f5285ccf4240")),
                                        SPEED, wrappedBuffer(decodeHexDump("3c00")),
                                        COURSE, wrappedBuffer(decodeHexDump("b400")),
                                        ALTITUDE, wrappedBuffer(decodeHexDump("00801643")),
                                        SATELLITE_COUNT, wrappedBuffer(decodeHexDump("08")),
                                        HDOP, wrappedBuffer(decodeHexDump("9a99993f")),
                                        BATTERY, wrappedBuffer(decodeHexDump("5f"))
                                )
                        ),
                        new LimeDataPackage(55.75, 37.62, (short) 60, (short) 180, 150.5F, (byte) 8, 1.2F, (byte) 95)
                ),
                Arguments.of(
                        new ValueSlices(
                                Map.of(
                                        LATITUDE, wrappedBuffer(decodeHexDump("0000000000e04b40")),
                                        LONGITUDE, wrappedBuffer(decodeHexDump("8fc2f5285ccf4240")),
                                        SPEED, wrappedBuffer(decodeHexDump("3c00")),
                                        COURSE, wrappedBuffer(decodeHexDump("b400")),
                                        ALTITUDE, wrappedBuffer(decodeHexDump("00801643")),
                                        SATELLITE_COUNT, wrappedBuffer(decodeHexDump("08"))
                                )
                        ),
                        new LimeDataPackage(55.75, 37.62, (short) 60, (short) 180, 150.5F, (byte) 8, null, null)
                )
        );
    }
}
