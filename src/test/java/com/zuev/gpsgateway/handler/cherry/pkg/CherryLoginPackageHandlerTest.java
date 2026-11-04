package com.zuev.gpsgateway.handler.cherry.pkg;

import com.zuev.gpsgateway.exception.PasswordNotSupportedException;
import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.model.cherry.CherryLoginPackage;
import com.zuev.gpsgateway.model.cherry.CherryLoginResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class CherryLoginPackageHandlerTest {
    private final CherryLoginPackageHandler handler = new CherryLoginPackageHandler(null, null);

    @Test
    public void imeiShouldBeGot() {
        CherryLoginPackage givenPackage = new CherryLoginPackage("555555555555555");

        String actual = handler.getImei(givenPackage);
        String expected = "555555555555555";
        assertEquals(expected, actual);
    }

    @Test
    public void passwordShouldBeGot() {
        CherryLoginPackage givenPackage = mock(CherryLoginPackage.class);

        String actual = handler.getPassword(givenPackage);
        assertNull(actual);

        verifyNoInteractions(givenPackage);
    }

    @Test
    public void successResponseShouldBeCreated() {
        CherryLoginPackage givenPackage = mock(CherryLoginPackage.class);
        Tracker givenTracker = mock(Tracker.class);

        CherryLoginResponse actual = handler.createSuccessResponse(givenPackage, givenTracker);
        CherryLoginResponse expected = new CherryLoginResponse((byte) 0);
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage, givenTracker);
    }

    @Test
    public void unknownImeiResponseShouldBeCreated() {
        CherryLoginPackage givenPackage = mock(CherryLoginPackage.class);

        CherryLoginResponse actual = handler.createUnknownImeiResponse(givenPackage);
        CherryLoginResponse expected = new CherryLoginResponse((byte) 1);
        assertEquals(expected, actual);

        verifyNoInteractions(givenPackage);
    }

    @Test
    public void wrongPasswordResponseShouldNotBeCreated() {
        CherryLoginPackage givenPackage = mock(CherryLoginPackage.class);

        assertThrows(PasswordNotSupportedException.class, () -> handler.createWrongPasswordResponse(givenPackage));

        verifyNoInteractions(givenPackage);
    }
}
