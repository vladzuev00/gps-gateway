package com.zuev.gpsgateway.handler.mango.pkg;

import com.zuev.gpsgateway.handler.base.pkg.PingPackageHandler;
import com.zuev.gpsgateway.model.mango.MangoPingPackage;
import com.zuev.gpsgateway.model.mango.MangoPingResponse;
import org.springframework.stereotype.Component;

@Component
public final class MangoPingPackageHandler extends PingPackageHandler<MangoPingPackage> {

    public MangoPingPackageHandler() {
        super(MangoPingPackage.class);
    }

    @Override
    protected MangoPingResponse createResponse(MangoPingPackage pkg) {
        return new MangoPingResponse();
    }
}
