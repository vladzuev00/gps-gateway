package com.zuev.gpsgateway.decoder.vzgp1.pkg;

import com.zuev.gpsgateway.model.vzgp1.VZGP1BlackBoxPackage;
import com.zuev.gpsgateway.model.vzgp1.VZGP1Data;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class VZGP1BlackBoxPackageDecoderTest {

    @Mock
    private VZGP1DataDecoder mockedDataDecoder;

    private VZGP1BlackBoxPackageDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new VZGP1BlackBoxPackageDecoder(mockedDataDecoder);
    }

    @Test
    public void payloadShouldBeDecoded() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("0002"));

        VZGP1Data givenFirstData = mock(VZGP1Data.class);
        VZGP1Data givenSecondData = mock(VZGP1Data.class);
        when(mockedDataDecoder.read(same(givenByteBuf))).thenReturn(givenFirstData).thenReturn(givenSecondData);

        VZGP1BlackBoxPackage actual = decoder.decodePayload(givenByteBuf);
        VZGP1BlackBoxPackage expected = new VZGP1BlackBoxPackage(List.of(givenFirstData, givenSecondData));
        assertEquals(expected, actual);
    }
}
