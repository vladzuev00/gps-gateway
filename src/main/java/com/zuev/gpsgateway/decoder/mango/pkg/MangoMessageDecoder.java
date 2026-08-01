package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoMessage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class MangoMessageDecoder {
    private static final int SPEED_PRESENT_BIT = 0x01;
    private static final int COURSE_PRESENT_BIT = 0x02;
    private static final int ALTITUDE_PRESENT_BIT = 0x04;
    private static final int SATELLITE_COUNT_PRESENT_BIT = 0x08;
    private static final int HDOP_PRESENT_BIT = 0x10;
    private static final int IGNITION_PRESENT_BIT = 0x20;
    private static final int BATTERY_PRESENT_BIT = 0x40;

    public MangoMessage decode(ByteBuf byteBuf) {
        long epochMillis = byteBuf.readLong();
        double latitude = byteBuf.readDouble();
        double longitude = byteBuf.readDouble();
        byte presenceBitMask = byteBuf.readByte();
        Short speed = isPresent(presenceBitMask, SPEED_PRESENT_BIT) ? byteBuf.readShort() : null;
        Short course = isPresent(presenceBitMask, COURSE_PRESENT_BIT) ? byteBuf.readShort() : null;
        Float altitude = isPresent(presenceBitMask, ALTITUDE_PRESENT_BIT) ? byteBuf.readFloat() : null;
        Byte satelliteCount = isPresent(presenceBitMask, SATELLITE_COUNT_PRESENT_BIT) ? byteBuf.readByte() : null;
        Float hdop = isPresent(presenceBitMask, HDOP_PRESENT_BIT) ? byteBuf.readFloat() : null;
        Byte ignition = isPresent(presenceBitMask, IGNITION_PRESENT_BIT) ? byteBuf.readByte() : null;
        Byte battery = isPresent(presenceBitMask, BATTERY_PRESENT_BIT) ? byteBuf.readByte() : null;
        return new MangoMessage(
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

    private boolean isPresent(byte presenceBitMask, int bit) {
        return (presenceBitMask & bit) != 0;
    }
}
