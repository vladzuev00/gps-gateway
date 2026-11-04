package com.zuev.gpsgateway.handler.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryPingPackage;
import com.zuev.gpsgateway.model.cherry.CherryPingResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class CherryPingPackageHandlerTest {
    private final CherryPingPackageHandler handler = new CherryPingPackageHandler();

    @Test
    public void responseShouldBeCreated() {
        CherryPingPackage givenPackage = mock(CherryPingPackage.class);

        CherryPingResponse actual = handler.createResponse(givenPackage);
        CherryPingResponse expected = new CherryPingResponse();
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage);
    }
}
