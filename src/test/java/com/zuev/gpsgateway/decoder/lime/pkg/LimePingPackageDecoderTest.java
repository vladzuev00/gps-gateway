package com.zuev.gpsgateway.decoder.lime.pkg;

import com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder.ValueSlices;
import com.zuev.gpsgateway.model.lime.LimePingPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class LimePingPackageDecoderTest {
    private final LimePingPackageDecoder decoder = new LimePingPackageDecoder();

    @Test
    public void valueSlicesShouldBeDecoded() {
        ValueSlices givenSlices = mock(ValueSlices.class);

        LimePingPackage actual = decoder.decodeValueSlices(givenSlices);
        LimePingPackage expected = new LimePingPackage();
        assertEquals(expected, actual);

        verifyNoInteractions(givenSlices);
    }
}
