package com.zuev.gpsgateway.util;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CRC16UtilTest {

    @Test
    public void crcShouldBeCalculated() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("565a01000548656c6c6fbfc5"));
        int givenFromIndex = 2;
        int givenToIndex = 10;

        int actual = CRC16Util.calculate(givenByteBuf, givenFromIndex, givenToIndex);
        int expected = 0xBFC5;
        assertEquals(expected, actual);
    }
}
