package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryMessage;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import static java.lang.Double.parseDouble;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public final class CherryMessageDecoder {
    private static final DateTimeFormatter DATE_FORMATTER = ofPattern("ddMMyy");
    private static final DateTimeFormatter TIME_FORMATTER = ofPattern("HHmmss");

    public CherryMessage decode(Iterator<String> iterator) {
        Instant dateTime = decodeDateTime(iterator.next(), iterator.next());
        double latitude = decodeLatitude(iterator.next());
        double longitude = decodeLongitude(iterator.next());
        Short speed = decodeSpeed(iterator.next());
        Short course = decodeCourse(iterator.next());
        Float altitude = decodeAltitude(iterator.next());
        Byte satelliteCount = decodeSatelliteCount(iterator.next());
        Float hdop = decodeHdop(iterator.next());
        Byte ignition = decodeIgnition(iterator.next());
        Byte battery = decodeBattery(iterator.next());
        return new CherryMessage(dateTime, latitude, longitude, speed, course, altitude, satelliteCount, hdop, ignition, battery);
    }

    private Instant decodeDateTime(String dateField, String timeField) {
        LocalDate date = LocalDate.parse(dateField, DATE_FORMATTER);
        LocalTime time = LocalTime.parse(timeField, TIME_FORMATTER);
        return date.atTime(time).toInstant(UTC);
    }

    private double decodeLatitude(String field) {
        return parseDouble(field);
    }

    private double decodeLongitude(String field) {
        return parseDouble(field);
    }

    private Short decodeSpeed(String field) {
        return !field.isEmpty() ? Short.valueOf(field) : null;
    }

    private Short decodeCourse(String field) {
        return !field.isEmpty() ? Short.valueOf(field) : null;
    }

    private Float decodeAltitude(String field) {
        return !field.isEmpty() ? Float.valueOf(field) : null;
    }

    private Byte decodeSatelliteCount(String field) {
        return !field.isEmpty() ? Byte.valueOf(field) : null;
    }

    private Float decodeHdop(String field) {
        return !field.isEmpty() ? Float.valueOf(field) : null;
    }

    private Byte decodeIgnition(String field) {
        return !field.isEmpty() ? Byte.valueOf(field) : null;
    }

    private Byte decodeBattery(String field) {
        return !field.isEmpty() ? Byte.valueOf(field) : null;
    }
}
