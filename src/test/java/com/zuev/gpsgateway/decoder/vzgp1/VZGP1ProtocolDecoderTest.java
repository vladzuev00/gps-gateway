package com.zuev.gpsgateway.decoder.vzgp1;

import com.zuev.gpsgateway.decoder.vzgp1.pkg.VZGP1AuthPackageDecoder;
import com.zuev.gpsgateway.decoder.vzgp1.pkg.VZGP1BlackBoxPackageDecoder;
import com.zuev.gpsgateway.decoder.vzgp1.pkg.VZGP1DataPackageDecoder;
import com.zuev.gpsgateway.decoder.vzgp1.pkg.VZGP1PingPackageDecoder;
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
public final class VZGP1ProtocolDecoderTest {

    @Mock
    private VZGP1AuthPackageDecoder mockedAuthPackageDecoder;

    @Mock
    private VZGP1PingPackageDecoder mockedPingPackageDecoder;

    @Mock
    private VZGP1DataPackageDecoder mockedDataPackageDecoder;

    @Mock
    private VZGP1BlackBoxPackageDecoder mockedBlackBoxPackageDecoder;

    private VZGP1ProtocolDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new VZGP1ProtocolDecoder(
                mockedAuthPackageDecoder,
                mockedPingPackageDecoder,
                mockedDataPackageDecoder,
                mockedBlackBoxPackageDecoder
        );
    }

    @Test
    public void completePackageEndShouldBeFound() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("565a020000802b565a01"));

        OptionalInt optionalActual = decoder.findCompletePackageEnd(givenByteBuf);
        assertTrue(optionalActual.isPresent());
        int actual = optionalActual.getAsInt();
        int expected = 6;
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(
                mockedAuthPackageDecoder,
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
                mockedAuthPackageDecoder,
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
                mockedAuthPackageDecoder,
                mockedPingPackageDecoder,
                mockedDataPackageDecoder,
                mockedBlackBoxPackageDecoder
        );
    }

    @Test
    public void checksumShouldBeGot() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("565a020000802b"));

        OptionalInt optionalActual = decoder.getChecksum(givenByteBuf);
        assertTrue(optionalActual.isPresent());
        int actual = optionalActual.getAsInt();
        int expected = 0x802B;
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(
                mockedAuthPackageDecoder,
                mockedPingPackageDecoder,
                mockedDataPackageDecoder,
                mockedBlackBoxPackageDecoder
        );
    }

    @Test
    public void checksumShouldBeCalculated() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("565a020000802b"));

        int actual = decoder.calculateChecksum(givenByteBuf);
        int expected = 0x802B;
        assertEquals(expected, actual);

        verifyNoInteractions(
                mockedAuthPackageDecoder,
                mockedPingPackageDecoder,
                mockedDataPackageDecoder,
                mockedBlackBoxPackageDecoder
        );
    }
}
