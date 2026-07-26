package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoMessage;
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
    private MangoMessageDecoder mockedMessageDecoder;

    private MangoDataPackageDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new MangoDataPackageDecoder(mockedMessageDecoder);
    }

    @Test
    public void payloadShouldBeDecoded() {
        ByteBuf givenPayload = mock(ByteBuf.class);

        MangoMessage givenMessage = mock(MangoMessage.class);
        when(mockedMessageDecoder.read(same(givenPayload))).thenReturn(givenMessage);

        MangoDataPackage actual = decoder.decodePayload(givenPayload);
        MangoDataPackage expected = new MangoDataPackage(givenMessage);
        assertEquals(expected, actual);

        verifyNoInteractions(givenPayload);
    }
}
