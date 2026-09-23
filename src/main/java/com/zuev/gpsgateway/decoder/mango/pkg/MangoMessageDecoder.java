package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoMessage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class MangoMessageDecoder {
    private static final byte SPEED_PRESENCE_MASK = 0x01;
    private static final byte COURSE_PRESENCE_MASK = 0x02;
    private static final byte ALTITUDE_PRESENCE_MASK = 0x04;
    private static final byte SATELLITE_COUNT_PRESENCE_MASK = 0x08;
    private static final byte HDOP_PRESENCE_MASK = 0x10;
    private static final byte IGNITION_PRESENCE_MASK = 0x20;
    private static final byte BATTERY_PRESENCE_MASK = 0x40;

    public MangoMessage decode(ByteBuf byteBuf) {
        long epochMillis = byteBuf.readLong();
        double latitude = byteBuf.readDouble();
        double longitude = byteBuf.readDouble();
        byte presenceFlags = byteBuf.readByte();
        Short speed = (presenceFlags & SPEED_PRESENCE_MASK) != 0 ? byteBuf.readShort() : null;
        Short course = (presenceFlags & COURSE_PRESENCE_MASK) != 0 ? byteBuf.readShort() : null;
        Float altitude = (presenceFlags & ALTITUDE_PRESENCE_MASK) != 0 ? byteBuf.readFloat() : null;
        Byte satelliteCount = (presenceFlags & SATELLITE_COUNT_PRESENCE_MASK) != 0 ? byteBuf.readByte() : null;
        Float hdop = (presenceFlags & HDOP_PRESENCE_MASK) != 0 ? byteBuf.readFloat() : null;
        Byte ignition = (presenceFlags & IGNITION_PRESENCE_MASK) != 0 ? byteBuf.readByte() : null;
        Byte battery = (presenceFlags & BATTERY_PRESENCE_MASK) != 0 ? byteBuf.readByte() : null;
        return new MangoMessage(epochMillis, latitude, longitude, speed, course, altitude, satelliteCount, hdop, ignition, battery);
    }
}
