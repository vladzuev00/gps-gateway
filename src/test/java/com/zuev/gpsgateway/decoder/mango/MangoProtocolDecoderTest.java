package com.zuev.gpsgateway.decoder.mango;

import com.zuev.gpsgateway.decoder.mango.pkg.MangoLoginPackageDecoder;
import com.zuev.gpsgateway.decoder.mango.pkg.MangoBlackBoxPackageDecoder;
import com.zuev.gpsgateway.decoder.mango.pkg.MangoDataPackageDecoder;
import com.zuev.gpsgateway.decoder.mango.pkg.MangoPingPackageDecoder;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.OptionalInt;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public final class MangoProtocolDecoderTest {

    @Mock
    private MangoLoginPackageDecoder mockedLoginPackageDecoder;

    @Mock
    private MangoPingPackageDecoder mockedPingPackageDecoder;

    @Mock
    private MangoDataPackageDecoder mockedDataPackageDecoder;

    @Mock
    private MangoBlackBoxPackageDecoder mockedBlackBoxPackageDecoder;

    private MangoProtocolDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new MangoProtocolDecoder(
                mockedLoginPackageDecoder,
                mockedPingPackageDecoder,
                mockedDataPackageDecoder,
                mockedBlackBoxPackageDecoder
        );
    }

    @Test
    public void completePackageEndShouldBeFound() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("565a0200000002565a01"));

        OptionalInt optionalActual = decoder.findCompletePackageEnd(givenByteBuf);
        assertTrue(optionalActual.isPresent());
        int actual = optionalActual.getAsInt();
        int expected = 6;
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(
                mockedLoginPackageDecoder,
                mockedPingPackageDecoder,
                mockedDataPackageDecoder,
                mockedBlackBoxPackageDecoder
        );
    }

    @Test
    public void completePackageEndShouldNotBeFoundBecauseOfReadableBytesIsLessThanEmptyPackageLength() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("565a02000080"));

        OptionalInt optionalActual = decoder.findCompletePackageEnd(givenByteBuf);
        assertTrue(optionalActual.isEmpty());
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(
                mockedLoginPackageDecoder,
                mockedPingPackageDecoder,
                mockedDataPackageDecoder,
                mockedBlackBoxPackageDecoder
        );
    }

    @Test
    public void completePackageEndShouldNotBeFoundBecauseOfNotCompleteBody() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("565a010014353535"));

        OptionalInt optionalActual = decoder.findCompletePackageEnd(givenByteBuf);
        assertTrue(optionalActual.isEmpty());
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(
                mockedLoginPackageDecoder,
                mockedPingPackageDecoder,
                mockedDataPackageDecoder,
                mockedBlackBoxPackageDecoder
        );
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

        verifyNoInteractions(
                mockedLoginPackageDecoder,
                mockedPingPackageDecoder,
                mockedDataPackageDecoder,
                mockedBlackBoxPackageDecoder
        );
    }

    @Test
    public void checksumShouldBeCalculated() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("565a0200000002"));

        int actual = decoder.calculateChecksum(givenByteBuf);
        int expected = 0x0002;
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(
                mockedLoginPackageDecoder,
                mockedPingPackageDecoder,
                mockedDataPackageDecoder,
                mockedBlackBoxPackageDecoder
        );
    }
}
