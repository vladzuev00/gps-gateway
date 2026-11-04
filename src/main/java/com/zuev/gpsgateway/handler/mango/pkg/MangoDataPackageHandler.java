package com.zuev.gpsgateway.handler.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoDataPackage;
import com.zuev.gpsgateway.model.mango.MangoDataResponse;
import com.zuev.gpsgateway.model.mango.MangoMessage;
import com.zuev.gpsgateway.service.MessageService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class MangoDataPackageHandler extends MangoMessagePackageHandler<MangoDataPackage> {

    public MangoDataPackageHandler(MessageService messageService) {
        super(MangoDataPackage.class, messageService);
    }

    @Override
    protected List<MangoMessage> getMessageSources(MangoDataPackage pkg) {
        return List.of(pkg.message());
    }

    @Override
    protected MangoDataResponse createResponse(MangoDataPackage pkg) {
        return new MangoDataResponse();
    }
}
