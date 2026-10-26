package com.zuev.gpsgateway.decoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeLoginPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder.Tags.IMEI;
import static com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder.Tags.PASSWORD;
import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class LimeLoginPackageDecoderTest {
    private final LimeLoginPackageDecoder decoder = new LimeLoginPackageDecoder();

    @Test
    public void valueSlicesShouldBeDecoded() {
        Map<Byte, ByteBuf> givenSlicesByTags = Map.of(
                IMEI, wrappedBuffer(decodeHexDump("353535353535353535353535353535")),
                PASSWORD, wrappedBuffer(decodeHexDump("0470617373"))
        );

        LimeLoginPackage actual = decoder.decodeValueSlices(givenSlicesByTags);
        LimeLoginPackage expected = new LimeLoginPackage("555555555555555", "pass");
        assertEquals(expected, actual);
    }
}
