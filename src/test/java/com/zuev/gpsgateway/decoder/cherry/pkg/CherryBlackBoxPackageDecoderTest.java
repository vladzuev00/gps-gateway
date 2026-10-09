package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryBlackBoxPackage;
import com.zuev.gpsgateway.model.cherry.CherryMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class CherryBlackBoxPackageDecoderTest {

    @Mock
    private CherryMessageDecoder mockedMessageDecoder;

    private CherryBlackBoxPackageDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new CherryBlackBoxPackageDecoder(mockedMessageDecoder);
    }

    @Test
    public void fieldsShouldBeDecoded() {
        Iterator<String> givenIterator = List.of("2").iterator();

        CherryMessage givenFirstMessage = mock(CherryMessage.class);
        CherryMessage givenSecondMessage = mock(CherryMessage.class);
        when(mockedMessageDecoder.decode(same(givenIterator)))
                .thenReturn(givenFirstMessage)
                .thenReturn(givenSecondMessage);

        CherryBlackBoxPackage actual = decoder.decodeFields(givenIterator);
        CherryBlackBoxPackage expected = new CherryBlackBoxPackage(List.of(givenFirstMessage, givenSecondMessage));
        assertEquals(expected, actual);
        assertFalse(givenIterator.hasNext());
    }
}
