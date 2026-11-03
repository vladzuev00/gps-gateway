package com.zuev.gpsgateway.handler.mango;

import com.zuev.gpsgateway.model.mango.MangoPingPackage;
import com.zuev.gpsgateway.model.mango.MangoPingResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class MangoPingPackageHandlerTest {
    private final MangoPingPackageHandler handler = new MangoPingPackageHandler();

    @Test
    public void responseShouldBeCreated() {
        MangoPingPackage givenPackage = mock(MangoPingPackage.class);

        MangoPingResponse actual = handler.createResponse(givenPackage);
        MangoPingResponse expected = new MangoPingResponse();
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage);
    }
}
