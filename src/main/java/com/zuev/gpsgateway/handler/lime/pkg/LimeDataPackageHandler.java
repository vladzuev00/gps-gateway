package com.zuev.gpsgateway.handler.lime.pkg;

import com.zuev.gpsgateway.handler.base.pkg.MessagePackageHandler;
import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.model.lime.LimeDataPackage;
import com.zuev.gpsgateway.model.lime.LimeDataResponse;
import com.zuev.gpsgateway.service.MessageService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.zuev.gpsgateway.model.MessageParameterNames.BATTERY;
import static java.lang.Double.parseDouble;
import static java.lang.System.currentTimeMillis;
import static java.util.Collections.emptyMap;

@Component
public final class LimeDataPackageHandler extends MessagePackageHandler<LimeDataPackage, LimeDataPackage> {

    public LimeDataPackageHandler(MessageService messageService) {
        super(LimeDataPackage.class, messageService);
    }

    @Override
    protected List<LimeDataPackage> getMessageSources(LimeDataPackage pkg) {
        return List.of(pkg);
    }

    @Override
    protected Message toMessage(LimeDataPackage source, long trackerId) {
        return new Message(
                source.longitude(),
                source.latitude(),
                currentTimeMillis(),
                (double) source.speed(),
                (double) source.course(),
                getAltitude(source),
                (int) source.satelliteCount(),
                getHdop(source),
                getAdditionalParametersByNames(source),
                trackerId
        );
    }

    @Override
    protected LimeDataResponse createResponse(LimeDataPackage pkg) {
        return new LimeDataResponse();
    }

    private double getAltitude(LimeDataPackage source) {
        return parseDouble(Float.toString(source.altitude()));
    }

    private Double getHdop(LimeDataPackage source) {
        return source.hdop() != null ? parseDouble(source.hdop().toString()) : null;
    }

    private Map<String, String> getAdditionalParametersByNames(LimeDataPackage source) {
        return source.battery() != null ? Map.of(BATTERY, source.battery().toString()) : emptyMap();
    }
}
