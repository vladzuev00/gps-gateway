package com.zuev.gpsgateway.decoder.lime.pkg;

//TODO refactor
public final class LimeTags {
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

    private LimeTags() {
    }
}
