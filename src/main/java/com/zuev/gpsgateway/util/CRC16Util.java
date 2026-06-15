package com.zuev.gpsgateway.util;

import io.netty.buffer.ByteBuf;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class CRC16Util {

    public static int calculate(ByteBuf byteBuf, int fromIndex, int toIndex) {
        int crc = 0x0000;
        for (int i = fromIndex; i < toIndex; i++) {
            crc ^= (byteBuf.getByte(i) & 0xFF) << 8;
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x8005;
                } else {
                    crc <<= 1;
                }
            }
        }
        return crc & 0xFFFF;
    }
}
