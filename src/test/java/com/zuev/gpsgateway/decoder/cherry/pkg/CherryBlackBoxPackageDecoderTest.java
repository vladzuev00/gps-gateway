package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryBlackBoxPackage;
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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class CherryBlackBoxPackageDecoderTest {

    @Mock
    private CherryMessageDecoder mockedMessageDecoder;

    @Captor
    private ArgumentCaptor<Iterator<String>> fieldIteratorCaptor;

    private CherryBlackBoxPackageDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new CherryBlackBoxPackageDecoder(mockedMessageDecoder);
    }

    @Test
    public void fieldsShouldBeDecoded() {
        String[] givenFields = {"2", "first", "second"};

        CherryMessage givenFirstMessage = mock(CherryMessage.class);
        CherryMessage givenSecondMessage = mock(CherryMessage.class);
        when(mockedMessageDecoder.decode(any()))
                .thenReturn(givenFirstMessage)
                .thenReturn(givenSecondMessage);

        CherryBlackBoxPackage actual = decoder.decodeFields(givenFields);
        CherryBlackBoxPackage expected = new CherryBlackBoxPackage(List.of(givenFirstMessage, givenSecondMessage));
        assertEquals(expected, actual);

        verify(mockedMessageDecoder, times(2)).decode(fieldIteratorCaptor.capture());
        List<Iterator<String>> actualPassedIterators = fieldIteratorCaptor.getAllValues();
        assertSame(actualPassedIterators.get(0), actualPassedIterators.get(1));
        List<String> actualRemainingFields = new ArrayList<>();
        actualPassedIterators.get(0).forEachRemaining(actualRemainingFields::add);
        assertEquals(List.of("first", "second"), actualRemainingFields);
    }
}
