package com.zuev.gpsgateway.handler.cherry.pkg;

import com.zuev.gpsgateway.handler.base.pkg.PingPackageHandler;
import com.zuev.gpsgateway.model.cherry.CherryPingPackage;
import com.zuev.gpsgateway.model.cherry.CherryPingResponse;
import org.springframework.stereotype.Component;

@Component
public final class CherryPingPackageHandler extends PingPackageHandler<CherryPingPackage> {

    public CherryPingPackageHandler() {
        super(CherryPingPackage.class);
    }

    @Override
    protected CherryPingResponse createResponse(CherryPingPackage pkg) {
        return new CherryPingResponse();
    }
}
