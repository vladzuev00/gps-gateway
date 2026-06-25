package com.zuev.gpsgateway.decoder.vzgp1.pkg;

import com.zuev.gpsgateway.model.vzgp1.VZGP1Data;
import com.zuev.gpsgateway.model.vzgp1.VZGP1DataPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class VZGP1DataPackageDecoderTest {

    @Mock
    private VZGP1DataDecoder mockedDataDecoder;

    private VZGP1DataPackageDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new VZGP1DataPackageDecoder(mockedDataDecoder);
    }

    @Test
    public void payloadShouldBeDecoded() {
        ByteBuf givenPayload = mock(ByteBuf.class);

        VZGP1Data givenData = mock(VZGP1Data.class);
        when(mockedDataDecoder.read(same(givenPayload))).thenReturn(givenData);

        VZGP1DataPackage actual = decoder.decodePayload(givenPayload);
        VZGP1DataPackage expected = new VZGP1DataPackage(givenData);
        assertEquals(expected, actual);

        verifyNoInteractions(givenPayload);
    }
}
