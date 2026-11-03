package com.zuev.gpsgateway.handler.mango;

import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.model.mango.MangoLoginPackage;
import com.zuev.gpsgateway.model.mango.MangoLoginResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class MangoLoginPackageHandlerTest {
    private final MangoLoginPackageHandler handler = new MangoLoginPackageHandler(null, null);

    @Test
    public void imeiShouldBeGot() {
        MangoLoginPackage givenPackage = new MangoLoginPackage("555555555555555", null);

        String actual = handler.getImei(givenPackage);
        String expected = "555555555555555";
        assertEquals(expected, actual);
    }

    @Test
    public void passwordShouldBeGot() {
        MangoLoginPackage givenPackage = new MangoLoginPackage(null, "pass");

        String actual = handler.getPassword(givenPackage);
        String expected = "pass";
        assertEquals(expected, actual);
    }

    @Test
    public void successResponseShouldBeCreated() {
        MangoLoginPackage givenPackage = mock(MangoLoginPackage.class);
        Tracker givenTracker = mock(Tracker.class);

        MangoLoginResponse actual = handler.createSuccessResponse(givenPackage, givenTracker);
        MangoLoginResponse expected = new MangoLoginResponse((byte) 0x00);
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage, givenTracker);
    }

    @Test
    public void unknownImeiResponseShouldBeCreated() {
        MangoLoginPackage givenPackage = mock(MangoLoginPackage.class);

        MangoLoginResponse actual = handler.createUnknownImeiResponse(givenPackage);
        MangoLoginResponse expected = new MangoLoginResponse((byte) 0x01);
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage);
    }

    @Test
    public void wrongPasswordResponseShouldBeCreated() {
        MangoLoginPackage givenPackage = mock(MangoLoginPackage.class);

        MangoLoginResponse actual = handler.createWrongPasswordResponse(givenPackage);
        MangoLoginResponse expected = new MangoLoginResponse((byte) 0x02);
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage);
    }
}
