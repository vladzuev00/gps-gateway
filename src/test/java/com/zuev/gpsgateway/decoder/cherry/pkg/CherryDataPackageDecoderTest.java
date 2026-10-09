package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryDataPackage;
import com.zuev.gpsgateway.model.cherry.CherryMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

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
        @SuppressWarnings("unchecked") Iterator<String> givenIterator = mock(Iterator.class);

        CherryMessage givenMessage = mock(CherryMessage.class);
        when(mockedMessageDecoder.decode(same(givenIterator))).thenReturn(givenMessage);

        CherryDataPackage actual = decoder.decodeFields(givenIterator);
        CherryDataPackage expected = new CherryDataPackage(givenMessage);
        assertEquals(expected, actual);

        verifyNoInteractions(givenIterator);
    }
}
