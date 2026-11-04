package com.zuev.gpsgateway.handler.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryDataPackage;
import com.zuev.gpsgateway.model.cherry.CherryDataResponse;
import com.zuev.gpsgateway.model.cherry.CherryMessage;
import com.zuev.gpsgateway.service.MessageService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class CherryDataPackageHandler extends CherryMessagePackageHandler<CherryDataPackage> {

    public CherryDataPackageHandler(MessageService messageService) {
        super(CherryDataPackage.class, messageService);
    }

    @Override
    protected List<CherryMessage> getMessageSources(CherryDataPackage pkg) {
        return List.of(pkg.message());
    }

    @Override
    protected CherryDataResponse createResponse(CherryDataPackage pkg) {
        return new CherryDataResponse();
    }
}
