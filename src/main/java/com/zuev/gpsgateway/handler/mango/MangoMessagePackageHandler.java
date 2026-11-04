package com.zuev.gpsgateway.handler.mango;

import com.zuev.gpsgateway.handler.base.MessagePackageHandler;
import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.model.mango.MangoMessage;
import com.zuev.gpsgateway.service.MessageService;

import java.util.HashMap;
import java.util.Map;

import static com.zuev.gpsgateway.model.MessageParameterNames.BATTERY;
import static com.zuev.gpsgateway.model.MessageParameterNames.IGNITION;
import static java.lang.Double.parseDouble;
import static java.util.Map.copyOf;

public abstract class MangoMessagePackageHandler<PKG> extends MessagePackageHandler<PKG, MangoMessage> {

    public MangoMessagePackageHandler(Class<PKG> packageType, MessageService messageService) {
        super(packageType, messageService);
    }

    @Override
    protected final Message toMessage(MangoMessage source, long trackerId) {
        return new Message(
                source.longitude(),
                source.latitude(),
                source.epochMillis(),
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

    private Map<String, String> getAdditionalParametersByNames(MangoMessage source) {
        Map<String, String> parametersByNames = new HashMap<>();
        if (source.ignition() != null) {
            parametersByNames.put(IGNITION, source.ignition().toString());
        }
        if (source.battery() != null) {
            parametersByNames.put(BATTERY, source.battery().toString());
        }
        return copyOf(parametersByNames);
    }
}
