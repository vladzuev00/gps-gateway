package com.zuev.gpsgateway.decoder.lime.pkg;

import com.zuev.gpsgateway.decoder.base.pkg.PackageDecoder;
import com.zuev.gpsgateway.exception.LimeUnknownTagException;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

import static com.zuev.gpsgateway.decoder.lime.pkg.LimeTags.*;

//TODO refactor
public abstract class LimePackageDecoder implements PackageDecoder<ByteBuf> {
    private static final int TAG_VALUE_PAIRS_LENGTH_LENGTH = 2;
    private static final int IMEI_LENGTH = 15;
    private static final int PASSWORD_LENGTH_LENGTH = 1;

    @Override
    public final Object decode(ByteBuf byteBuf) {
        Map<Byte, ByteBuf> valuesByTags = new HashMap<>();
        int index = byteBuf.readerIndex() + TAG_VALUE_PAIRS_LENGTH_LENGTH;
        while (index < byteBuf.writerIndex()) {
            byte tag = byteBuf.getByte(index++);
            int valueLength = switch (tag) {
                case IMEI -> IMEI_LENGTH;
                case PASSWORD -> PASSWORD_LENGTH_LENGTH + byteBuf.getUnsignedByte(index);
                case LATITUDE, LONGITUDE -> Double.BYTES;
                case SPEED, COURSE -> Short.BYTES;
                case ALTITUDE, HDOP -> Float.BYTES;
                case SATELLITE_COUNT, BATTERY -> Byte.BYTES;
                default -> throw new LimeUnknownTagException("Unknown tag %02x".formatted(tag));
            };
            valuesByTags.put(tag, byteBuf.slice(index, valueLength));
            index += valueLength;
        }
        return decodeTags(valuesByTags);
    }

    protected abstract Object decodeTags(Map<Byte, ByteBuf> valuesByTags);
}
