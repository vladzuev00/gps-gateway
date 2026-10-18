package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoMessage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MangoMessageDecoderTest {
    private final MangoMessageDecoder decoder = new MangoMessageDecoder();

    @ParameterizedTest
    @MethodSource("messageShouldBeDecodedMethodSource")
    public void messageShouldBeDecoded(String givenHexDump, MangoMessage expected, int expectedReaderIndex) {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump(givenHexDump));

        MangoMessage actual = decoder.decode(givenByteBuf);
        assertEquals(expected, actual);
        assertEquals(expectedReaderIndex, givenByteBuf.readerIndex());
    }

    private static Stream<Arguments> messageShouldBeDecodedMethodSource() {
        return Stream.of(
                Arguments.of(
                        "0000018bcfe56800404a0fcd67fd3f5b40107e6b3fe9fadb7f003c00b441280000083f99999a01550000018bcfe56be8404a53c0ca600b0340129e065300581500",
                        new MangoMessage(
                                1700000000000L,
                                52.123456,
                                4.123456,
                                (short) 60,
                                (short) 180,
                                10.5F,
                                (byte) 8,
                                1.2F,
                                (byte) 1,
                                (byte) 85
                        ),
                        40
                ),
                Arguments.of(
                        "0000018bcfe56800404a0fcd67fd3f5b40107e6b3fe9fadb000000018bcfe56be8404a53c0ca600b0340129e06530058157f003c00b441280000083f99999a0155",
                        new MangoMessage(
                                1700000000000L,
                                52.123456,
                                4.123456,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                        ),
                        25
                )
        );
    }
}
