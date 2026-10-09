package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryMessage;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public final class CherryMessageDecoderTest {
    private final CherryMessageDecoder decoder = new CherryMessageDecoder();

    @Test
    public void messageWithOptionalFieldsShouldBeDecoded() {
        var givenIterator = List.of("141123", "221320", "55.75", "37.62", "60", "180", "150.5", "8", "1.2", "1", "95")
                .iterator();

        CherryMessage actual = decoder.decode(givenIterator);
        CherryMessage expected = new CherryMessage(
                LocalDateTime.of(2023, 11, 14, 22, 13, 20),
                55.75,
                37.62,
                (short) 60,
                (short) 180,
                150.5F,
                (byte) 8,
                1.2F,
                (byte) 1,
                (byte) 95
        );
        assertEquals(expected, actual);
        assertFalse(givenIterator.hasNext());
    }

    @Test
    public void messageWithoutOptionalFieldsShouldBeDecoded() {
        var givenIterator = List.of("141123", "221325", "55.76", "37.63", "", "", "", "", "", "", "").iterator();

        CherryMessage actual = decoder.decode(givenIterator);
        CherryMessage expected = new CherryMessage(
                LocalDateTime.of(2023, 11, 14, 22, 13, 25),
                55.76,
                37.63,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertEquals(expected, actual);
        assertFalse(givenIterator.hasNext());
    }
}
