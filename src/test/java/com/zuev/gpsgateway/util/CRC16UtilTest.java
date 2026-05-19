package com.zuev.gpsgateway.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CRC16UtilTest {

    @Test
    public void crcShouldBeCalculated() {
        byte[] givenBytes = {0x56, 0x5A, 0x01, 0x00, 0x05, 0x48, 0x65, 0x6C, 0x6C, 0x6F, (byte) 0xBF, (byte) 0xC5};
        int givenFromIndex = 2;
        int givenToIndex = 10;

        int actual = CRC16Util.calculate(givenBytes, givenFromIndex, givenToIndex);
        int expected = 0xBFC5;
        assertEquals(expected, actual);
    }
}
