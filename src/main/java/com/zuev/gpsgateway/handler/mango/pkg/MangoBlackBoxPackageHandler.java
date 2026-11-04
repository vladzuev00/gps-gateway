package com.zuev.gpsgateway.handler.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoBlackBoxPackage;
import com.zuev.gpsgateway.model.mango.MangoBlackBoxResponse;
import com.zuev.gpsgateway.model.mango.MangoMessage;
import com.zuev.gpsgateway.service.MessageService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class MangoBlackBoxPackageHandler extends MangoMessagePackageHandler<MangoBlackBoxPackage> {

    public MangoBlackBoxPackageHandler(MessageService messageService) {
        super(MangoBlackBoxPackage.class, messageService);
    }

    @Override
    protected List<MangoMessage> getMessageSources(MangoBlackBoxPackage pkg) {
        return pkg.messages();
    }

    @Override
    protected MangoBlackBoxResponse createResponse(MangoBlackBoxPackage pkg) {
        return new MangoBlackBoxResponse(pkg.messages().size());
    }
}
