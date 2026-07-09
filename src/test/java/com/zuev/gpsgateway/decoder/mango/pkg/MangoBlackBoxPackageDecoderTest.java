package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoBlackBoxPackage;
import com.zuev.gpsgateway.model.mango.MangoData;
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
public final class MangoBlackBoxPackageDecoderTest {

    @Mock
    private MangoDataDecoder mockedDataDecoder;

    private MangoBlackBoxPackageDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new MangoBlackBoxPackageDecoder(mockedDataDecoder);
    }

    @Test
    public void payloadShouldBeDecoded() {
        ByteBuf givenPayload = wrappedBuffer(decodeHexDump("0002"));

        MangoData givenFirstData = mock(MangoData.class);
        MangoData givenSecondData = mock(MangoData.class);
        when(mockedDataDecoder.read(same(givenPayload))).thenReturn(givenFirstData).thenReturn(givenSecondData);

        MangoBlackBoxPackage actual = decoder.decodePayload(givenPayload);
        MangoBlackBoxPackage expected = new MangoBlackBoxPackage(List.of(givenFirstData, givenSecondData));
        assertEquals(expected, actual);
    }
}
