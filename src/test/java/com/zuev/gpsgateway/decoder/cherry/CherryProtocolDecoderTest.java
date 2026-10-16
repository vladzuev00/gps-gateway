package com.zuev.gpsgateway.decoder.cherry;

import com.zuev.gpsgateway.decoder.cherry.pkg.CherryPackageDecoder;
import com.zuev.gpsgateway.exception.InvalidChecksumException;
import com.zuev.gpsgateway.exception.TooLongPackageException;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.OptionalInt;

import static com.zuev.gpsgateway.decoder.cherry.CherryProtocolDecoder.CHARSET;
import static com.zuev.gpsgateway.decoder.cherry.CherryProtocolDecoder.MAX_INCOMPLETE_PACKAGE_LENGTH;
import static io.netty.buffer.Unpooled.copiedBuffer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;

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
        var givenByteBuf = copiedBuffer("\0@PING@430\0@AUTH@123456789012345;pass;1771\0@PI", CHARSET).readerIndex(1);

        OptionalInt optionalActual = decoder.findCompletePackageEnd(givenByteBuf);
        assertTrue(optionalActual.isPresent());
        int actual = optionalActual.getAsInt();
        int expected = 10;
        assertEquals(expected, actual);
        assertEquals(1, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @Test
    public void completePackageEndShouldNotBeFoundBecauseIncompletePackageExceedsMaxLength() {
        var givenByteBuf = copiedBuffer("\0" + "@".repeat(MAX_INCOMPLETE_PACKAGE_LENGTH + 1), CHARSET).readerIndex(1);

        assertThrows(TooLongPackageException.class, () -> decoder.findCompletePackageEnd(givenByteBuf));
        assertEquals(1, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @Test
    public void completePackageEndShouldNotBeFound() {
        ByteBuf givenByteBuf = copiedBuffer("\0@AUTH@123456789012345;pa", CHARSET).readerIndex(1);

        OptionalInt optionalActual = decoder.findCompletePackageEnd(givenByteBuf);
        assertTrue(optionalActual.isEmpty());
        assertEquals(1, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @ParameterizedTest
    @CsvSource({"'@AUTH@123456789012345;pass;1771\0', 1771", "'@PING@430\0', 430"})
    public void checksumShouldBeGot(String givenText, int expected) {
        ByteBuf givenByteBuf = copiedBuffer(givenText, CHARSET);

        OptionalInt optionalActual = decoder.getChecksum(givenByteBuf);
        assertTrue(optionalActual.isPresent());
        int actual = optionalActual.getAsInt();
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @ParameterizedTest
    @ValueSource(strings = {"@AUTH@123456789012345;pass;\0", "@AUTH@123456789012345;pass;17a1\0", "@PING430\0"})
    public void checksumShouldNotBeGotBecausePackageIsCorrupted(String givenText) {
        ByteBuf givenByteBuf = copiedBuffer(givenText, CHARSET);

        assertThrows(InvalidChecksumException.class, () -> decoder.getChecksum(givenByteBuf));
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @ParameterizedTest
    @CsvSource({"'@AUTH@123456789012345;pass;0\0', 1771", "'@PING@0\0', 430"})
    public void checksumShouldBeCalculated(String givenText, int expected) {
        ByteBuf givenByteBuf = copiedBuffer(givenText, CHARSET);

        int actual = decoder.calculateChecksum(givenByteBuf);
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @Test
    public void checksumShouldNotBeCalculatedBecausePackageIsCorrupted() {
        ByteBuf givenByteBuf = copiedBuffer("@PING430\0", CHARSET);

        assertThrows(IllegalStateException.class, () -> decoder.calculateChecksum(givenByteBuf));
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }
}
