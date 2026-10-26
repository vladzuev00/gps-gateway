package com.zuev.gpsgateway.decoder.lime.pkg;

import com.zuev.gpsgateway.decoder.base.pkg.PackageDecoder;
import com.zuev.gpsgateway.exception.LimeUnknownTagException;
import io.netty.buffer.ByteBuf;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder.Tags.*;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
public abstract class LimePackageDecoder implements PackageDecoder<ByteBuf> {
    protected static final int TAG_VALUE_PAIRS_LENGTH_LENGTH = 2;
    protected static final int IMEI_LENGTH = 15;
    protected static final int PASSWORD_LENGTH_LENGTH = 1;

    private final Set<Byte> packageTags;

    @Override
    public final boolean isAbleDecode(ByteBuf byteBuf) {
        int firstTagIndex = byteBuf.readerIndex() + TAG_VALUE_PAIRS_LENGTH_LENGTH;
        if (firstTagIndex == byteBuf.writerIndex()) {
            return packageTags.isEmpty();
        }
        byte firstTag = byteBuf.getByte(firstTagIndex);
        return packageTags.contains(firstTag);
    }

    @Override
    public final Object decode(ByteBuf byteBuf) {
        byteBuf.skipBytes(TAG_VALUE_PAIRS_LENGTH_LENGTH);
        Map<Byte, ByteBuf> valueSlicesByTags = new HashMap<>();
        while (byteBuf.isReadable()) {
            Map.Entry<Byte, ByteBuf> valueSliceByTag = readValueSliceByTag(byteBuf);
            valueSlicesByTags.put(valueSliceByTag.getKey(), valueSliceByTag.getValue());
        }
        return decodeValueSlices(valueSlicesByTags);
    }

    protected abstract Object decodeValueSlices(Map<Byte, ByteBuf> slicesByTags);

    private Map.Entry<Byte, ByteBuf> readValueSliceByTag(ByteBuf byteBuf) {
        byte tag = byteBuf.readByte();
        int valueLength = switch (tag) {
            case IMEI -> IMEI_LENGTH;
            case PASSWORD -> PASSWORD_LENGTH_LENGTH + byteBuf.getUnsignedByte(byteBuf.readerIndex());
            case LATITUDE, LONGITUDE -> Double.BYTES;
            case SPEED, COURSE -> Short.BYTES;
            case ALTITUDE, HDOP -> Float.BYTES;
            case SATELLITE_COUNT, BATTERY -> Byte.BYTES;
            default -> throw new LimeUnknownTagException("Unknown tag %02x".formatted(tag));
        };
        ByteBuf valueSlice = byteBuf.readSlice(valueLength);
        return Map.entry(tag, valueSlice);
    }

    @NoArgsConstructor(access = PRIVATE)
    protected static final class Tags {
        public static final byte IMEI = 0x01;
        public static final byte PASSWORD = 0x02;
        public static final byte LATITUDE = 0x04;
        public static final byte LONGITUDE = 0x05;
        public static final byte SPEED = 0x06;
        public static final byte COURSE = 0x07;
        public static final byte ALTITUDE = 0x08;
        public static final byte SATELLITE_COUNT = 0x09;
        public static final byte HDOP = 0x0A;
        public static final byte BATTERY = 0x0B;
    }
}
