package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.function.Function;

import static java.lang.Double.parseDouble;
import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public final class CherryMessageDecoder {
    private static final DateTimeFormatter DATE_FORMATTER = ofPattern("ddMMyy");
    private static final DateTimeFormatter TIME_FORMATTER = ofPattern("HHmmss");

    public CherryMessage decode(Iterator<String> iterator) {
        LocalDateTime dateTime = decodeDateTime(iterator.next(), iterator.next());
        double latitude = decodeLatitude(iterator.next());
        double longitude = decodeLongitude(iterator.next());
        Short speed = decodeSpeed(iterator.next());
        Short course = decodeCourse(iterator.next());
        Float altitude = decodeAltitude(iterator.next());

        Byte satelliteCount = decodeOptional(iterator.next(), Byte::valueOf);
        Float hdop = decodeOptional(iterator.next(), Float::valueOf);
        Byte ignition = decodeOptional(iterator.next(), Byte::valueOf);
        Byte battery = decodeOptional(iterator.next(), Byte::valueOf);
        return new CherryMessage(dateTime, latitude, longitude, speed, course, altitude, satelliteCount, hdop, ignition, battery);
    }

    private LocalDateTime decodeDateTime(String dateField, String timeField) {
        LocalDate date = LocalDate.parse(dateField, DATE_FORMATTER);
        LocalTime time = LocalTime.parse(timeField, TIME_FORMATTER);
        return LocalDateTime.of(date, time);
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

    private static <T> T decodeOptional(String field, Function<String, T> parser) {
        return !field.isEmpty() ? parser.apply(field) : null;
    }
}
