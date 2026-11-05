package com.zuev.gpsgateway.handler.lime.pkg;

import com.zuev.gpsgateway.handler.base.pkg.LoginPackageHandler;
import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.model.lime.LimeLoginPackage;
import com.zuev.gpsgateway.model.lime.LimeLoginResponse;
import com.zuev.gpsgateway.registry.TrackerChannelRegistry;
import com.zuev.gpsgateway.service.TrackerLoginService;
import org.springframework.stereotype.Component;

@Component
public final class LimeLoginPackageHandler extends LoginPackageHandler<LimeLoginPackage> {
    private static final byte SUCCESS_STATUS = 0x00;
    private static final byte UNKNOWN_IMEI_STATUS = 0x01;
    private static final byte WRONG_PASSWORD_STATUS = 0x02;

    public LimeLoginPackageHandler(TrackerLoginService trackerLoginService,
                                   TrackerChannelRegistry trackerChannelRegistry) {
        super(LimeLoginPackage.class, trackerLoginService, trackerChannelRegistry);
    }

    @Override
    protected String getImei(LimeLoginPackage pkg) {
        return pkg.imei();
    }

    @Override
    protected String getPassword(LimeLoginPackage pkg) {
        return pkg.password();
    }

    @Override
    protected LimeLoginResponse createSuccessResponse(LimeLoginPackage pkg, Tracker tracker) {
        return new LimeLoginResponse(SUCCESS_STATUS);
    }

    @Override
    protected LimeLoginResponse createUnknownImeiResponse(LimeLoginPackage pkg) {
        return new LimeLoginResponse(UNKNOWN_IMEI_STATUS);
    }

    @Override
    protected LimeLoginResponse createWrongPasswordResponse(LimeLoginPackage pkg) {
        return new LimeLoginResponse(WRONG_PASSWORD_STATUS);
    }
}
