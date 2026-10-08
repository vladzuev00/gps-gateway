package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryDataPackage;
import com.zuev.gpsgateway.model.cherry.CherryMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class CherryDataPackageDecoderTest {

    @Mock
    private CherryMessageDecoder mockedMessageDecoder;

    private CherryDataPackageDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new CherryDataPackageDecoder(mockedMessageDecoder);
    }

    @Test
    public void fieldsShouldBeDecoded() {
        Iterator<String> givenFields = List.of("first", "second").iterator();

        CherryMessage givenMessage = mock(CherryMessage.class);
        when(mockedMessageDecoder.decode(same(givenFields))).thenReturn(givenMessage);

        CherryDataPackage actual = decoder.decodeFields(givenFields);
        CherryDataPackage expected = new CherryDataPackage(givenMessage);
        assertEquals(expected, actual);
    }
}
