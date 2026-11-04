package com.zuev.gpsgateway.handler.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoBlackBoxPackage;
import com.zuev.gpsgateway.model.mango.MangoBlackBoxResponse;
import com.zuev.gpsgateway.model.mango.MangoMessage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

public final class MangoBlackBoxPackageHandlerTest {
    private final MangoBlackBoxPackageHandler handler = new MangoBlackBoxPackageHandler(null);

    @Test
    public void messageSourcesShouldBeGot() {
        List<MangoMessage> givenMessages = List.of(mock(MangoMessage.class), mock(MangoMessage.class));
        MangoBlackBoxPackage givenPackage = new MangoBlackBoxPackage(givenMessages);

        List<MangoMessage> actual = handler.getMessageSources(givenPackage);
        assertSame(givenMessages, actual);
    }

    @Test
    public void responseShouldBeCreated() {
        var givenPackage = new MangoBlackBoxPackage(List.of(mock(MangoMessage.class), mock(MangoMessage.class)));

        MangoBlackBoxResponse actual = handler.createResponse(givenPackage);
        MangoBlackBoxResponse expected = new MangoBlackBoxResponse(2);
        assertEquals(expected, actual);
    }
}
