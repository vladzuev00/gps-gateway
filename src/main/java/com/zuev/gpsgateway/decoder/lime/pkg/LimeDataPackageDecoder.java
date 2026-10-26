package com.zuev.gpsgateway.decoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeDataPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder.Tags.*;

@Component
public final class LimeDataPackageDecoder extends LimePackageDecoder {

    public LimeDataPackageDecoder() {
        super(Set.of(LATITUDE, LONGITUDE, SPEED, COURSE, ALTITUDE, SATELLITE_COUNT, HDOP, BATTERY));
    }

    @Override
    protected LimeDataPackage decodeValueSlices(ValueSlices slices) {
        double latitude = decodeLatitude(slices);
        double longitude = decodeLongitude(slices);
        short speed = decodeSpeed(slices);
        short course = decodeCourse(slices);
        float altitude = decodeAltitude(slices);
        byte satelliteCount = decodeSatelliteCount(slices);
        Float hdop = decodeHdop(slices);
        Byte battery = decodeBattery(slices);
        return new LimeDataPackage(latitude, longitude, speed, course, altitude, satelliteCount, hdop, battery);
    }

    private double decodeLatitude(ValueSlices slices) {
        return slices.getRequired(LATITUDE).readDoubleLE();
    }

    private double decodeLongitude(ValueSlices slices) {
        return slices.getRequired(LONGITUDE).readDoubleLE();
    }

    private short decodeSpeed(ValueSlices slices) {
        return slices.getRequired(SPEED).readShortLE();
    }

    private short decodeCourse(ValueSlices slices) {
        return slices.getRequired(COURSE).readShortLE();
    }

    private float decodeAltitude(ValueSlices slices) {
        return slices.getRequired(ALTITUDE).readFloatLE();
    }

    private byte decodeSatelliteCount(ValueSlices slices) {
        return slices.getRequired(SATELLITE_COUNT).readByte();
    }

    private Float decodeHdop(ValueSlices slices) {
        ByteBuf slice = slices.getOptional(HDOP);
        return slice != null ? slice.readFloatLE() : null;
    }

    private Byte decodeBattery(ValueSlices slices) {
        ByteBuf slice = slices.getOptional(BATTERY);
        return slice != null ? slice.readByte() : null;
    }
}
