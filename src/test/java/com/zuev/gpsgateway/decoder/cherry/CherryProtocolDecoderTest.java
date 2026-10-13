package com.zuev.gpsgateway.decoder.cherry;

import com.zuev.gpsgateway.decoder.cherry.pkg.CherryPackageDecoder;
import com.zuev.gpsgateway.exception.InvalidChecksumException;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.OptionalInt;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;

//TODO refactor
@ExtendWith(MockitoExtension.class)
public final class CherryProtocolDecoderTest {

    @Mock
    private CherryPackageDecoder mockedFirstPackageDecoder;

    @Mock
    private CherryPackageDecoder mockedSecondPackageDecoder;

    private CherryProtocolDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new CherryProtocolDecoder(List.of(mockedFirstPackageDecoder, mockedSecondPackageDecoder));
    }

    @Test
    public void completePackageEndShouldBeFound() {
        ByteBuf givenByteBuf = copiedBuffer("\0@PING@\0@AUTH@123456789012345;pass;1771\0@PI", US_ASCII).readerIndex(1);

        OptionalInt optionalActual = decoder.findCompletePackageEnd(givenByteBuf);
        assertTrue(optionalActual.isPresent());
        int actual = optionalActual.getAsInt();
        int expected = 7;
        assertEquals(expected, actual);
        assertEquals(1, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @Test
    public void completePackageEndShouldNotBeFound() {
        ByteBuf givenByteBuf = copiedBuffer("\0@AUTH@123456789012345;pa", US_ASCII).readerIndex(1);

        OptionalInt optionalActual = decoder.findCompletePackageEnd(givenByteBuf);
        assertTrue(optionalActual.isEmpty());
        assertEquals(1, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @Test
    public void checksumShouldBeGot() {
        ByteBuf givenByteBuf = copiedBuffer("@AUTH@123456789012345;pass;1771\0", US_ASCII);

        OptionalInt optionalActual = decoder.getChecksum(givenByteBuf);
        assertTrue(optionalActual.isPresent());
        int actual = optionalActual.getAsInt();
        int expected = 1771;
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @Test
    public void checksumShouldNotBeGotBecauseThereAreNoFieldsInPackage() {
        ByteBuf givenByteBuf = copiedBuffer("@PING@\0", US_ASCII);

        OptionalInt optionalActual = decoder.getChecksum(givenByteBuf);
        assertTrue(optionalActual.isEmpty());
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "17a1"})
    public void checksumShouldNotBeGotBecauseItIsCorrupted(String checksum) {
        ByteBuf givenByteBuf = copiedBuffer("@AUTH@123456789012345;pass;" + checksum + "\0", US_ASCII);

        var actual = assertThrows(InvalidChecksumException.class, () -> decoder.getChecksum(givenByteBuf));
        assertInstanceOf(NumberFormatException.class, actual.getCause());
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @Test
    public void checksumShouldBeCalculated() {
        ByteBuf givenByteBuf = copiedBuffer("@AUTH@123456789012345;pass;1771\0", US_ASCII);

        int actual = decoder.calculateChecksum(givenByteBuf);
        int expected = 1771;
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }
}
