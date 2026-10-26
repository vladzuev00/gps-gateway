package com.zuev.gpsgateway.decoder.lime.pkg;

import com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder.ValueSlices;
import com.zuev.gpsgateway.model.lime.LimeLoginPackage;
import org.junit.jupiter.api.Test;

import static com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder.Tags.IMEI;
import static com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder.Tags.PASSWORD;
import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class LimeLoginPackageDecoderTest {
    private final LimeLoginPackageDecoder decoder = new LimeLoginPackageDecoder();

    @Test
    public void valueSlicesShouldBeDecoded() {
        ValueSlices givenSlices = mock(ValueSlices.class);

        when(givenSlices.getRequired(IMEI)).thenReturn(wrappedBuffer(decodeHexDump("353535353535353535353535353535")));
        when(givenSlices.getRequired(PASSWORD)).thenReturn(wrappedBuffer(decodeHexDump("0470617373")));

        LimeLoginPackage actual = decoder.decodeValueSlices(givenSlices);
        LimeLoginPackage expected = new LimeLoginPackage("555555555555555", "pass");
        assertEquals(expected, actual);
    }
}
