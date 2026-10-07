package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryDataPackage;
import com.zuev.gpsgateway.model.cherry.CherryMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class CherryDataPackageDecoderTest {

    @Mock
    private CherryMessageDecoder mockedMessageDecoder;

    @Captor
    private ArgumentCaptor<Iterator<String>> fieldIteratorCaptor;

    private CherryDataPackageDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new CherryDataPackageDecoder(mockedMessageDecoder);
    }

    @Test
    public void fieldsShouldBeDecoded() {
        String[] givenFields = {"first", "second"};

        CherryMessage givenMessage = mock(CherryMessage.class);
        when(mockedMessageDecoder.decode(any())).thenReturn(givenMessage);

        CherryDataPackage actual = decoder.decodeFields(givenFields);
        CherryDataPackage expected = new CherryDataPackage(givenMessage);
        assertEquals(expected, actual);

        verify(mockedMessageDecoder).decode(fieldIteratorCaptor.capture());
        List<String> actualPassedFields = new ArrayList<>();
        fieldIteratorCaptor.getValue().forEachRemaining(actualPassedFields::add);
        assertEquals(List.of("first", "second"), actualPassedFields);
    }
}
