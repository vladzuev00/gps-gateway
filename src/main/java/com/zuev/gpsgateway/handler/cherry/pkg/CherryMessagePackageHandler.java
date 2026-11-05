package com.zuev.gpsgateway.handler.cherry.pkg;

import com.zuev.gpsgateway.handler.base.pkg.MessagePackageHandler;
import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.model.cherry.CherryMessage;
import com.zuev.gpsgateway.service.MessageService;

import java.util.HashMap;
import java.util.Map;

import static com.zuev.gpsgateway.model.MessageAdditionalParameterNames.IGNITION;
import static com.zuev.gpsgateway.model.MessageAdditionalParameterNames.ODOMETER;
import static java.lang.Double.parseDouble;
import static java.util.Map.copyOf;

public abstract class CherryMessagePackageHandler<PKG> extends MessagePackageHandler<PKG, CherryMessage> {

    public CherryMessagePackageHandler(Class<PKG> packageType, MessageService messageService) {
        super(packageType, messageService);
    }

    @Override
    protected final Message toMessage(CherryMessage source, long trackerId) {
        return new Message(
                source.longitude(),
                source.latitude(),
                source.dateTime().toEpochMilli(),
                toNullableDouble(source.speed()),
                toNullableDouble(source.course()),
                toNullableDouble(source.altitude()),
                toNullableInteger(source.satelliteCount()),
                toNullableDouble(source.hdop()),
                getAdditionalParametersByNames(source),
                trackerId
        );
    }

    private Double toNullableDouble(Number value) {
        return value != null ? parseDouble(value.toString()) : null;
    }

    private Integer toNullableInteger(Byte value) {
        return value != null ? value.intValue() : null;
    }

    private Map<String, String> getAdditionalParametersByNames(CherryMessage source) {
        Map<String, String> parametersByNames = new HashMap<>();
        if (source.ignition() != null) {
            parametersByNames.put(IGNITION, source.ignition().toString());
        }
        if (source.odometer() != null) {
            parametersByNames.put(ODOMETER, source.odometer().toString());
        }
        return copyOf(parametersByNames);
    }
}
