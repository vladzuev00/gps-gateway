package com.zuev.gpsgateway.decoder.mango;

import com.zuev.gpsgateway.decoder.mango.pkg.MangoPackageDecoder;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public final class MangoProtocolDecoderTest {

    @Mock
    private MangoPackageDecoder mockedFirstPackageDecoder;

    @Mock
    private MangoPackageDecoder mockedSecondPackageDecoder;

    private MangoProtocolDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new MangoProtocolDecoder(List.of(mockedFirstPackageDecoder, mockedSecondPackageDecoder));
    }

    @Test
    public void completePackageEndShouldBeFound() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("0000565a0200000002565a01")).readerIndex(2);

        OptionalInt optionalActual = decoder.findCompletePackageEnd(givenByteBuf);
        assertTrue(optionalActual.isPresent());
        int actual = optionalActual.getAsInt();
        int expected = 8;
        assertEquals(expected, actual);
        assertEquals(2, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @ParameterizedTest
    @ValueSource(strings = {"565a02000080", "565a010014353535"})
    public void completePackageEndShouldNotBeFound(String givenHexDump) {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump(givenHexDump));

        OptionalInt optionalActual = decoder.findCompletePackageEnd(givenByteBuf);
        assertTrue(optionalActual.isEmpty());
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @Test
    public void checksumShouldBeGot() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("565a0200000002"));

        OptionalInt optionalActual = decoder.getChecksum(givenByteBuf);
        assertTrue(optionalActual.isPresent());
        int actual = optionalActual.getAsInt();
        int expected = 0x0002;
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @ParameterizedTest
    @MethodSource("checksumShouldBeCalculatedMethodSource")
    public void checksumShouldBeCalculated(String givenHexDump, int expected) {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump(givenHexDump));

        int actual = decoder.calculateChecksum(givenByteBuf);
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    private static Stream<Arguments> checksumShouldBeCalculatedMethodSource() {
        return Stream.of(
                Arguments.of("565a010014353535353535353535353535353535047061737304eb", 0x04eb),
                Arguments.of("565a020101" + "ff".repeat(257) + "0003", 0x0003)
        );
    }
}
