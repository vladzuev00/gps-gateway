package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.function.Function;

import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public final class CherryMessageDecoder {
    private static final DateTimeFormatter DATE_FORMATTER = ofPattern("ddMMyy");
    private static final DateTimeFormatter TIME_FORMATTER = ofPattern("HHmmss");

    public CherryMessage decode(Iterator<String> fields) {
        LocalDate date = LocalDate.parse(fields.next(), DATE_FORMATTER);
        LocalTime time = LocalTime.parse(fields.next(), TIME_FORMATTER);
        double latitude = Double.parseDouble(fields.next());
        double longitude = Double.parseDouble(fields.next());
        Short speed = decodeOptional(fields.next(), Short::valueOf);
        Short course = decodeOptional(fields.next(), Short::valueOf);
        Float altitude = decodeOptional(fields.next(), Float::valueOf);
        Byte satelliteCount = decodeOptional(fields.next(), Byte::valueOf);
        Float hdop = decodeOptional(fields.next(), Float::valueOf);
        Byte ignition = decodeOptional(fields.next(), Byte::valueOf);
        Byte battery = decodeOptional(fields.next(), Byte::valueOf);
        return new CherryMessage(
                LocalDateTime.of(date, time),
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

    private static <T> T decodeOptional(String field, Function<String, T> parser) {
        return !field.isEmpty() ? parser.apply(field) : null;
    }
}
