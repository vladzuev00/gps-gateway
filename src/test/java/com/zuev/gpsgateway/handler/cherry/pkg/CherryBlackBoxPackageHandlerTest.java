package com.zuev.gpsgateway.handler.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryBlackBoxPackage;
import com.zuev.gpsgateway.model.cherry.CherryBlackBoxResponse;
import com.zuev.gpsgateway.model.cherry.CherryMessage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class CherryBlackBoxPackageHandlerTest {
    private final CherryBlackBoxPackageHandler handler = new CherryBlackBoxPackageHandler(null);

    @Test
    public void messageSourcesShouldBeGot() {
        List<CherryMessage> givenMessages = List.of(mock(CherryMessage.class), mock(CherryMessage.class));
        CherryBlackBoxPackage givenPackage = new CherryBlackBoxPackage(givenMessages);

        List<CherryMessage> actual = handler.getMessageSources(givenPackage);
        assertSame(givenMessages, actual);
    }

    @Test
    public void responseShouldBeCreated() {
        CherryBlackBoxPackage givenPackage = mock(CherryBlackBoxPackage.class);

        CherryBlackBoxResponse actual = handler.createResponse(givenPackage);
        CherryBlackBoxResponse expected = new CherryBlackBoxResponse();
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage);
    }
}
