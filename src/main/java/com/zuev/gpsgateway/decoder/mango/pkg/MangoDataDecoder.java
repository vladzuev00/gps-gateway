package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoData;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class MangoDataDecoder {
    private static final int SPEED_PRESENT_BIT = 0x01;
    private static final int COURSE_PRESENT_BIT = 0x02;
    private static final int ALTITUDE_PRESENT_BIT = 0x04;
    private static final int SATELLITE_COUNT_PRESENT_BIT = 0x08;
    private static final int HDOP_PRESENT_BIT = 0x10;
    private static final int IGNITION_PRESENT_BIT = 0x20;
    private static final int BATTERY_PRESENT_BIT = 0x40;

    public MangoData read(ByteBuf byteBuf) {
        long epochMillis = byteBuf.readLong();
        double latitude = byteBuf.readDouble();
        double longitude = byteBuf.readDouble();
        byte bitMask = byteBuf.readByte();
        Short speed = isPresent(bitMask, SPEED_PRESENT_BIT) ? byteBuf.readShort() : null;
        Short course = isPresent(bitMask, COURSE_PRESENT_BIT) ? byteBuf.readShort() : null;
        Float altitude = isPresent(bitMask, ALTITUDE_PRESENT_BIT) ? byteBuf.readFloat() : null;
        Byte satelliteCount = isPresent(bitMask, SATELLITE_COUNT_PRESENT_BIT) ? byteBuf.readByte() : null;
        Float hdop = isPresent(bitMask, HDOP_PRESENT_BIT) ? byteBuf.readFloat() : null;
        Byte ignition = isPresent(bitMask, IGNITION_PRESENT_BIT) ? byteBuf.readByte() : null;
        Byte battery = isPresent(bitMask, BATTERY_PRESENT_BIT) ? byteBuf.readByte() : null;
        return new MangoData(
                epochMillis,
                latitude,
                longitude,
                speed,
                course,
                altitude,
                satelliteCount,
                hdop,
                ignition,
                battery
        );
    }

    private boolean isPresent(byte mask, int bit) {
        return (mask & bit) != 0;
    }
}
