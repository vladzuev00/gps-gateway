package com.zuev.gpsgateway.handler.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryBlackBoxPackage;
import com.zuev.gpsgateway.model.cherry.CherryBlackBoxResponse;
import com.zuev.gpsgateway.model.cherry.CherryMessage;
import com.zuev.gpsgateway.service.MessageService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class CherryBlackBoxPackageHandler extends CherryMessagePackageHandler<CherryBlackBoxPackage> {

    public CherryBlackBoxPackageHandler(MessageService messageService) {
        super(CherryBlackBoxPackage.class, messageService);
    }

    @Override
    protected List<CherryMessage> getMessageSources(CherryBlackBoxPackage pkg) {
        return pkg.messages();
    }

    @Override
    protected CherryBlackBoxResponse createResponse(CherryBlackBoxPackage pkg) {
        return new CherryBlackBoxResponse();
    }
}
