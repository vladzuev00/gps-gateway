package com.zuev.gpsgateway.handler.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryDataPackage;
import com.zuev.gpsgateway.model.cherry.CherryDataResponse;
import com.zuev.gpsgateway.model.cherry.CherryMessage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class CherryDataPackageHandlerTest {
    private final CherryDataPackageHandler handler = new CherryDataPackageHandler(null);

    @Test
    public void messageSourcesShouldBeGot() {
        CherryMessage givenMessage = mock(CherryMessage.class);
        CherryDataPackage givenPackage = new CherryDataPackage(givenMessage);

        List<CherryMessage> actual = handler.getMessageSources(givenPackage);
        List<CherryMessage> expected = List.of(givenMessage);
        assertEquals(expected, actual);
    }

    @Test
    public void responseShouldBeCreated() {
        CherryDataPackage givenPackage = mock(CherryDataPackage.class);

        CherryDataResponse actual = handler.createResponse(givenPackage);
        CherryDataResponse expected = new CherryDataResponse();
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage);
    }
}
