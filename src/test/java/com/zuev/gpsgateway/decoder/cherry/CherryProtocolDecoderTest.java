package com.zuev.gpsgateway.decoder.cherry;

import com.zuev.gpsgateway.decoder.cherry.pkg.CherryAuthPackageDecoder;
import com.zuev.gpsgateway.decoder.cherry.pkg.CherryBlackBoxPackageDecoder;
import com.zuev.gpsgateway.decoder.cherry.pkg.CherryDataPackageDecoder;
import com.zuev.gpsgateway.decoder.cherry.pkg.CherryMessageDecoder;
import com.zuev.gpsgateway.decoder.cherry.pkg.CherryPingPackageDecoder;
import com.zuev.gpsgateway.exception.InvalidChecksumException;
import com.zuev.gpsgateway.model.cherry.CherryAuthPackage;
import com.zuev.gpsgateway.model.cherry.CherryBlackBoxPackage;
import com.zuev.gpsgateway.model.cherry.CherryDataPackage;
import com.zuev.gpsgateway.model.cherry.CherryMessage;
import com.zuev.gpsgateway.model.cherry.CherryPingPackage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalInt;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public final class CherryProtocolDecoderTest {

    @Mock
    private CherryAuthPackageDecoder mockedAuthPackageDecoder;

    @Mock
    private CherryPingPackageDecoder mockedPingPackageDecoder;

    @Mock
    private CherryDataPackageDecoder mockedDataPackageDecoder;

    @Mock
    private CherryBlackBoxPackageDecoder mockedBlackBoxPackageDecoder;

    private CherryProtocolDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new CherryProtocolDecoder(
                List.of(
                        mockedAuthPackageDecoder,
                        mockedPingPackageDecoder,
                        mockedDataPackageDecoder,
                        mockedBlackBoxPackageDecoder
                )
        );
    }

    @Test
    public void completePackageEndShouldBeFound() {
        ByteBuf givenByteBuf = copiedBuffer("@PING@\0@AUTH@", US_ASCII);

        OptionalInt optionalActual = decoder.findCompletePackageEnd(givenByteBuf);
        assertTrue(optionalActual.isPresent());
        int actual = optionalActual.getAsInt();
        int expected = 6;
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoPackageDecoderInteractions();
    }

    @Test
    public void completePackageEndShouldNotBeFound() {
        ByteBuf givenByteBuf = copiedBuffer("@AUTH@123456789012345;pa", US_ASCII);

        OptionalInt optionalActual = decoder.findCompletePackageEnd(givenByteBuf);
        assertTrue(optionalActual.isEmpty());
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoPackageDecoderInteractions();
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

        verifyNoPackageDecoderInteractions();
    }

    @Test
    public void checksumShouldNotBeGotBecauseOfPackageWithoutFields() {
        ByteBuf givenByteBuf = copiedBuffer("@PING@\0", US_ASCII);

        OptionalInt optionalActual = decoder.getChecksum(givenByteBuf);
        assertTrue(optionalActual.isEmpty());
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoPackageDecoderInteractions();
    }

    @Test
    public void checksumShouldBeCalculated() {
        ByteBuf givenByteBuf = copiedBuffer("@AUTH@123456789012345;pass;1771\0", US_ASCII);

        int actual = decoder.calculateChecksum(givenByteBuf);
        int expected = 1771;
        assertEquals(expected, actual);
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoPackageDecoderInteractions();
    }

    @Test
    public void packagesShouldBeDecodedFromChannel() {
        CherryMessageDecoder messageDecoder = new CherryMessageDecoder();
        EmbeddedChannel givenChannel = new EmbeddedChannel(
                new CherryProtocolDecoder(
                        List.of(
                                new CherryAuthPackageDecoder(),
                                new CherryPingPackageDecoder(),
                                new CherryDataPackageDecoder(messageDecoder),
                                new CherryBlackBoxPackageDecoder(messageDecoder)
                        )
                )
        );
        ByteBuf givenByteBuf = copiedBuffer(
                "@AUTH@123456789012345;pass;1771\0"
                        + "@PING@\0"
                        + "@DATA@141123;221320;55.75;37.62;60;180;150.5;8;1.2;1;95;3037\0"
                        + "@BLACKBOX@2;141123;221320;55.75;37.62;60;180;150.5;8;1.2;1;95;"
                        + "141123;221325;55.76;37.63;;;;;;;;5216\0",
                US_ASCII
        );

        assertTrue(givenChannel.writeInbound(givenByteBuf));

        CherryMessage firstMessage = new CherryMessage(
                LocalDateTime.of(2023, 11, 14, 22, 13, 20),
                55.75,
                37.62,
                (short) 60,
                (short) 180,
                150.5F,
                (byte) 8,
                1.2F,
                (byte) 1,
                (byte) 95
        );
        CherryMessage secondMessage = new CherryMessage(
                LocalDateTime.of(2023, 11, 14, 22, 13, 25),
                55.76,
                37.63,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertEquals(new CherryAuthPackage("123456789012345", "pass"), givenChannel.readInbound());
        assertEquals(new CherryPingPackage(), givenChannel.readInbound());
        assertEquals(new CherryDataPackage(firstMessage), givenChannel.readInbound());
        assertEquals(
                new CherryBlackBoxPackage(List.of(firstMessage, secondMessage)),
                givenChannel.readInbound()
        );
        assertNull(givenChannel.readInbound());
    }

    @Test
    public void packageShouldNotBeDecodedFromChannelBecauseOfNotValidChecksum() {
        EmbeddedChannel givenChannel = new EmbeddedChannel(decoder);
        ByteBuf givenByteBuf = copiedBuffer("@AUTH@123456789012345;pass;1772\0", US_ASCII);

        DecoderException actual = assertThrows(DecoderException.class, () -> givenChannel.writeInbound(givenByteBuf));
        assertInstanceOf(InvalidChecksumException.class, actual.getCause());

        verifyNoPackageDecoderInteractions();
    }

    private void verifyNoPackageDecoderInteractions() {
        verifyNoInteractions(
                mockedAuthPackageDecoder,
                mockedPingPackageDecoder,
                mockedDataPackageDecoder,
                mockedBlackBoxPackageDecoder
        );
    }
}
