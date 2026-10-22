package com.zuev.gpsgateway.decoder.lime;

import com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder;
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

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public final class LimeProtocolDecoderTest {

    @Mock
    private LimePackageDecoder mockedFirstPackageDecoder;

    @Mock
    private LimePackageDecoder mockedSecondPackageDecoder;

    private LimeProtocolDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new LimeProtocolDecoder(List.of(mockedFirstPackageDecoder, mockedSecondPackageDecoder));
    }

    @Test
    public void completePackageEndShouldBeFound() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("00001600013535353535353535353535353535350204706173730000"))
                .readerIndex(2);

        OptionalInt optionalActual = decoder.findCompletePackageEnd(givenByteBuf);
        assertTrue(optionalActual.isPresent());
        int actual = optionalActual.getAsInt();
        int expected = 25;
        assertEquals(expected, actual);
        assertEquals(2, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @ParameterizedTest
    @ValueSource(strings = {"16", "160001353535353535353535"})
    public void completePackageEndShouldNotBeFound(String givenHexDump) {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump(givenHexDump));

        OptionalInt optionalActual = decoder.findCompletePackageEnd(givenByteBuf);
        assertTrue(optionalActual.isEmpty());
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @Test
    public void checksumShouldNotBeGot() {
        ByteBuf givenByteBuf = mock(ByteBuf.class);

        OptionalInt optionalActual = decoder.getChecksum(givenByteBuf);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(givenByteBuf, mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }

    @Test
    public void calculatedChecksumShouldBeDummy() {
        ByteBuf givenByteBuf = mock(ByteBuf.class);

        int actual = decoder.calculateChecksum(givenByteBuf);
        int expected = Integer.MIN_VALUE;
        assertEquals(expected, actual);

        verifyNoInteractions(givenByteBuf, mockedFirstPackageDecoder, mockedSecondPackageDecoder);
    }
}
