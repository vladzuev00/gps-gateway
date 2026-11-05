package com.zuev.gpsgateway.handler.lime.pkg;

import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.model.lime.LimeLoginPackage;
import com.zuev.gpsgateway.model.lime.LimeLoginResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class LimeLoginPackageHandlerTest {
    private final LimeLoginPackageHandler handler = new LimeLoginPackageHandler(null, null);

    @Test
    public void imeiShouldBeGot() {
        LimeLoginPackage givenPackage = new LimeLoginPackage("555555555555555", null);

        String actual = handler.getImei(givenPackage);
        String expected = "555555555555555";
        assertEquals(expected, actual);
    }

    @Test
    public void passwordShouldBeGot() {
        LimeLoginPackage givenPackage = new LimeLoginPackage(null, "pass");

        String actual = handler.getPassword(givenPackage);
        String expected = "pass";
        assertEquals(expected, actual);
    }

    @Test
    public void successResponseShouldBeCreated() {
        LimeLoginPackage givenPackage = mock(LimeLoginPackage.class);
        Tracker givenTracker = mock(Tracker.class);

        LimeLoginResponse actual = handler.createSuccessResponse(givenPackage, givenTracker);
        LimeLoginResponse expected = new LimeLoginResponse((byte) 0x00);
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage, givenTracker);
    }

    @Test
    public void unknownImeiResponseShouldBeCreated() {
        LimeLoginPackage givenPackage = mock(LimeLoginPackage.class);

        LimeLoginResponse actual = handler.createUnknownImeiResponse(givenPackage);
        LimeLoginResponse expected = new LimeLoginResponse((byte) 0x01);
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage);
    }

    @Test
    public void wrongPasswordResponseShouldBeCreated() {
        LimeLoginPackage givenPackage = mock(LimeLoginPackage.class);

        LimeLoginResponse actual = handler.createWrongPasswordResponse(givenPackage);
        LimeLoginResponse expected = new LimeLoginResponse((byte) 0x02);
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage);
    }
}
