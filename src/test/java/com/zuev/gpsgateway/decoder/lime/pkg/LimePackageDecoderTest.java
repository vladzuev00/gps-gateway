package com.zuev.gpsgateway.decoder.lime.pkg;

import com.zuev.gpsgateway.exception.LimeUnknownTagException;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder.Tags.*;
import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.*;

public final class LimePackageDecoderTest {
    private final LimePackageDecoder allTagsDecoder = new TestLimeAllTagsPackageDecoder();
    private final LimePackageDecoder noTagsDecoder = new TestLimeNoTagsPackageDecoder();

    @Test
    public void decoderShouldBeAbleToDecodeByteBuf() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("160001353535353535353535353535353535020470617373"));

        assertTrue(allTagsDecoder.isAbleDecode(givenByteBuf));
        assertEquals(0, givenByteBuf.readerIndex());
    }

    @Test
    public void decoderShouldNotBeAbleToDecodeByteBuf() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("1600AA353535353535353535353535353535020470617373"));

        assertFalse(allTagsDecoder.isAbleDecode(givenByteBuf));
        assertEquals(0, givenByteBuf.readerIndex());
    }

    @Test
    public void decoderShouldBeAbleToDecodeByteBufWithoutTags() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("0000"));

        assertTrue(noTagsDecoder.isAbleDecode(givenByteBuf));
        assertEquals(0, givenByteBuf.readerIndex());
    }

    @Test
    public void decoderShouldNotBeAbleToDecodeByteBufWithoutTags() {
        LimePackageDecoder givenDecoder = new LimePackageDecoder(Set.of(IMEI)) {

            @Override
            protected Object decodeValueSlices(Map<Byte, ByteBuf> slicesByTags) {
                throw new UnsupportedOperationException();
            }
        };
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("0000"));

        assertFalse(givenDecoder.isAbleDecode(givenByteBuf));
        assertEquals(0, givenByteBuf.readerIndex());
    }

    @Test
    public void byteBufShouldBeDecoded() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("3c0001353535353535353535353535353535020470617373040000000000e04b40058fc2f5285ccf4240063c0007b400080080164309080a9a99993f0b5f"));

        Object actual = allTagsDecoder.decode(givenByteBuf);
        TestLimeAllTagsPackage expected = new TestLimeAllTagsPackage(
                "555555555555555",
                "pass",
                55.75,
                37.62,
                (short) 60,
                (short) 180,
                150.5F,
                (byte) 8,
                1.2F,
                (byte) 95
        );
        assertEquals(expected, actual);
        assertFalse(givenByteBuf.isReadable());
    }

    @Test
    public void byteBufWithoutTagsShouldBeDecoded() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("0000"));

        Object actual = noTagsDecoder.decode(givenByteBuf);
        TestLimeNoTagsPackage expected = new TestLimeNoTagsPackage();
        assertEquals(expected, actual);
        assertFalse(givenByteBuf.isReadable());
    }

    @Test
    public void byteBufShouldNotBeDecodedBecauseOfUnknownTag() {
        ByteBuf givenByteBuf = wrappedBuffer(decodeHexDump("0100AA"));

        assertThrows(LimeUnknownTagException.class, () -> allTagsDecoder.decode(givenByteBuf));
        assertFalse(givenByteBuf.isReadable());
    }

    private record TestLimeAllTagsPackage(String imei,
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

    private record TestLimeNoTagsPackage() {
    }

    private static final class TestLimeAllTagsPackageDecoder extends LimePackageDecoder {

        public TestLimeAllTagsPackageDecoder() {
            super(Set.of(IMEI, PASSWORD, LATITUDE, LONGITUDE, SPEED, COURSE, ALTITUDE, SATELLITE_COUNT, HDOP, BATTERY));
        }

        @Override
        protected TestLimeAllTagsPackage decodeValueSlices(Map<Byte, ByteBuf> slicesByTags) {
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
            return new TestLimeAllTagsPackage(imei, password, latitude, longitude, speed, course, altitude, satelliteCount, hdop, battery);
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

    private static final class TestLimeNoTagsPackageDecoder extends LimePackageDecoder {

        public TestLimeNoTagsPackageDecoder() {
            super(emptySet());
        }

        @Override
        protected TestLimeNoTagsPackage decodeValueSlices(Map<Byte, ByteBuf> slicesByTags) {
            return new TestLimeNoTagsPackage();
        }
    }
}
