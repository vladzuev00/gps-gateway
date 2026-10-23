package com.zuev.gpsgateway.decoder.lime.pkg;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder.Tags.*;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.Collections.emptySet;

//TODO refactor
public final class LimePackageDecoderTest {

    @Test
    public void first() {
        //TODO протестить декодирование всех всех полей(isAbleDecode)
    }

    @Test
    public void second() {
        //TODO протестить декодирование 0 полей(isAbleDecode)
    }

    @Test
    public void third() {
        //TODO протестить декодирование всех всех полей(decode)
    }


    private record TestAllTagsLimePackage(String imei,
                                          String password,
                                          double latitude,
                                          double longitude,
                                          short speed,
                                          short course,
                                          float altitude,
                                          byte satelliteCount,
                                          float hdop,
                                          byte battery) {
    }

    private record TestNoTagsLimePackage() {
    }

    private static final class TestAllTagsLimePackageDecoder extends LimePackageDecoder {

        public TestAllTagsLimePackageDecoder() {
            super(Set.of(IMEI, PASSWORD, LATITUDE, LONGITUDE, SPEED, COURSE, ALTITUDE, SATELLITE_COUNT, HDOP, BATTERY));
        }

        @Override
        protected TestAllTagsLimePackage decodeValueSlices(Map<Byte, ByteBuf> slicesByTags) {
            String imei = decodeImei(slicesByTags);
            String password = decodePassword(slicesByTags);
            double latitude = decodeLatitude(slicesByTags);
            double longitude = decodeLongitude(slicesByTags);
            short speed = decodeSpeed(slicesByTags);
            short course = decodeCourse(slicesByTags);
            float altitude = decodeAltitude(slicesByTags);
            byte satelliteCount = decodeSatelliteCount(slicesByTags);
            float hdop = decodeHdop(slicesByTags);
            byte battery = decodeBattery(slicesByTags);
            return new TestAllTagsLimePackage(imei, password, latitude, longitude, speed, course, altitude, satelliteCount, hdop, battery);
        }

        private String decodeImei(Map<Byte, ByteBuf> slicesByTags) {
            return slicesByTags.get(IMEI).toString(US_ASCII);
        }

        private String decodePassword(Map<Byte, ByteBuf> slicesByTags) {
            return slicesByTags.get(PASSWORD).skipBytes(PASSWORD_LENGTH_LENGTH).toString(US_ASCII);
        }

        private double decodeLatitude(Map<Byte, ByteBuf> slicesByTags) {
            return slicesByTags.get(LATITUDE).readDoubleLE();
        }

        private double decodeLongitude(Map<Byte, ByteBuf> slicesByTags) {
            return slicesByTags.get(LONGITUDE).readDoubleLE();
        }

        private short decodeSpeed(Map<Byte, ByteBuf> slicesByTags) {
            return slicesByTags.get(SPEED).readShortLE();
        }

        private short decodeCourse(Map<Byte, ByteBuf> slicesByTags) {
            return slicesByTags.get(COURSE).readShortLE();
        }

        private float decodeAltitude(Map<Byte, ByteBuf> slicesByTags) {
            return slicesByTags.get(ALTITUDE).readFloatLE();
        }

        private byte decodeSatelliteCount(Map<Byte, ByteBuf> slicesByTags) {
            return slicesByTags.get(SATELLITE_COUNT).readByte();
        }

        private float decodeHdop(Map<Byte, ByteBuf> slicesByTags) {
            return slicesByTags.get(HDOP).readFloatLE();
        }

        private byte decodeBattery(Map<Byte, ByteBuf> slicesByTags) {
            return slicesByTags.get(BATTERY).readByte();
        }
    }

    private static final class TestNoTagsLimePackageDecoder extends LimePackageDecoder {

        public TestNoTagsLimePackageDecoder() {
            super(emptySet());
        }

        @Override
        protected TestNoTagsLimePackage decodeValueSlices(Map<Byte, ByteBuf> slicesByTags) {
            return new TestNoTagsLimePackage();
        }
    }
}
