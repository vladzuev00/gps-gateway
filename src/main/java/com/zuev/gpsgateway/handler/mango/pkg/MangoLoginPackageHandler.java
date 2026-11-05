package com.zuev.gpsgateway.handler.mango.pkg;

import com.zuev.gpsgateway.handler.base.pkg.LoginPackageHandler;
import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.model.mango.MangoLoginPackage;
import com.zuev.gpsgateway.model.mango.MangoLoginResponse;
import com.zuev.gpsgateway.registry.TrackerChannelRegistry;
import com.zuev.gpsgateway.service.TrackerLoginService;
import org.springframework.stereotype.Component;

@Component
public final class MangoLoginPackageHandler extends LoginPackageHandler<MangoLoginPackage> {
    private static final byte SUCCESS_STATUS = 0x00;
    private static final byte UNKNOWN_IMEI_STATUS = 0x01;
    private static final byte WRONG_PASSWORD_STATUS = 0x02;

    public MangoLoginPackageHandler(TrackerLoginService trackerLoginService,
                                    TrackerChannelRegistry trackerChannelRegistry) {
        super(MangoLoginPackage.class, trackerLoginService, trackerChannelRegistry);
    }

    @Override
    protected String getImei(MangoLoginPackage pkg) {
        return pkg.imei();
    }

    @Override
    protected String getPassword(MangoLoginPackage pkg) {
        return pkg.password();
    }

    @Override
    protected MangoLoginResponse createSuccessResponse(MangoLoginPackage pkg, Tracker tracker) {
        return new MangoLoginResponse(SUCCESS_STATUS);
    }

    @Override
    protected MangoLoginResponse createUnknownImeiResponse(MangoLoginPackage pkg) {
        return new MangoLoginResponse(UNKNOWN_IMEI_STATUS);
    }

    @Override
    protected MangoLoginResponse createWrongPasswordResponse(MangoLoginPackage pkg) {
        return new MangoLoginResponse(WRONG_PASSWORD_STATUS);
    }
}
