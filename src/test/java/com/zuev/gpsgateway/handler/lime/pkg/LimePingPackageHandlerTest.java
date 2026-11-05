package com.zuev.gpsgateway.handler.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimePingPackage;
import com.zuev.gpsgateway.model.lime.LimePingResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class LimePingPackageHandlerTest {
    private final LimePingPackageHandler handler = new LimePingPackageHandler();

    @Test
    public void responseShouldBeCreated() {
        LimePingPackage givenPackage = mock(LimePingPackage.class);

        LimePingResponse actual = handler.createResponse(givenPackage);
        LimePingResponse expected = new LimePingResponse();
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage);
    }
}
