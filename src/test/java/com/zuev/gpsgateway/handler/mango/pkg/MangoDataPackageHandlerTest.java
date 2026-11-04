package com.zuev.gpsgateway.handler.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoDataPackage;
import com.zuev.gpsgateway.model.mango.MangoDataResponse;
import com.zuev.gpsgateway.model.mango.MangoMessage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class MangoDataPackageHandlerTest {
    private final MangoDataPackageHandler handler = new MangoDataPackageHandler(null);

    @Test
    public void messageSourcesShouldBeGot() {
        MangoMessage givenMessage = mock(MangoMessage.class);
        MangoDataPackage givenPackage = new MangoDataPackage(givenMessage);

        List<MangoMessage> actual = handler.getMessageSources(givenPackage);
        List<MangoMessage> expected = List.of(givenMessage);
        assertEquals(expected, actual);
    }

    @Test
    public void responseShouldBeCreated() {
        MangoDataPackage givenPackage = mock(MangoDataPackage.class);

        MangoDataResponse actual = handler.createResponse(givenPackage);
        MangoDataResponse expected = new MangoDataResponse();
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage);
    }
}
