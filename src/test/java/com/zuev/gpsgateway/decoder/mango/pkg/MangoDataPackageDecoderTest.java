package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoData;
import com.zuev.gpsgateway.model.mango.MangoDataPackage;
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
public final class MangoDataPackageDecoderTest {

    @Mock
    private MangoDataDecoder mockedDataDecoder;

    private MangoDataPackageDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new MangoDataPackageDecoder(mockedDataDecoder);
    }

    @Test
    public void payloadShouldBeDecoded() {
        ByteBuf givenPayload = mock(ByteBuf.class);

        MangoData givenData = mock(MangoData.class);
        when(mockedDataDecoder.read(same(givenPayload))).thenReturn(givenData);

        MangoDataPackage actual = decoder.decodePayload(givenPayload);
        MangoDataPackage expected = new MangoDataPackage(givenData);
        assertEquals(expected, actual);

        verifyNoInteractions(givenPayload);
    }
}
