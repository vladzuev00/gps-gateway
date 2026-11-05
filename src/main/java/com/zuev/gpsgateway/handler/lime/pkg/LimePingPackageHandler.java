package com.zuev.gpsgateway.handler.lime.pkg;

import com.zuev.gpsgateway.handler.base.pkg.PingPackageHandler;
import com.zuev.gpsgateway.model.lime.LimePingPackage;
import com.zuev.gpsgateway.model.lime.LimePingResponse;
import org.springframework.stereotype.Component;

@Component
public final class LimePingPackageHandler extends PingPackageHandler<LimePingPackage> {

    public LimePingPackageHandler() {
        super(LimePingPackage.class);
    }

    @Override
    protected LimePingResponse createResponse(LimePingPackage pkg) {
        return new LimePingResponse();
    }
}
