package com.zuev.gpsgateway.handler.mango;

import com.zuev.gpsgateway.handler.base.MessagePackageHandler;
import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.model.mango.MangoMessage;
import com.zuev.gpsgateway.service.MessageService;

import java.util.HashMap;
import java.util.Map;

import static com.zuev.gpsgateway.model.MessageParameterNames.BATTERY;
import static com.zuev.gpsgateway.model.MessageParameterNames.IGNITION;

//TODO refactor
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
                toDouble(source.speed()),
                toDouble(source.course()),
                toDouble(source.altitude()),
                toUnsignedInteger(source.satelliteCount()),
                toDouble(source.hdop()),
                getAdditionalParametersByNames(source),
                trackerId
        );
    }

    private static Map<String, String> getAdditionalParametersByNames(MangoMessage source) {
        Map<String, String> parametersByNames = new HashMap<>();
        putIfNotNull(parametersByNames, IGNITION, toUnsignedInteger(source.ignition()));
        putIfNotNull(parametersByNames, BATTERY, toUnsignedInteger(source.battery()));
        return Map.copyOf(parametersByNames);
    }

    private static void putIfNotNull(Map<String, String> parametersByNames, String name, Object value) {
        if (value != null) {
            parametersByNames.put(name, value.toString());
        }
    }

    private static Double toDouble(Number value) {
        return value != null ? value.doubleValue() : null;
    }

    private static Integer toUnsignedInteger(Byte value) {
        return value != null ? Byte.toUnsignedInt(value) : null;
    }
}
