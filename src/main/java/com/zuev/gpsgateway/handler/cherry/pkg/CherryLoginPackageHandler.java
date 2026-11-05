package com.zuev.gpsgateway.handler.cherry.pkg;

import com.zuev.gpsgateway.exception.PasswordNotSupportedException;
import com.zuev.gpsgateway.handler.base.pkg.LoginPackageHandler;
import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.model.cherry.CherryLoginPackage;
import com.zuev.gpsgateway.model.cherry.CherryLoginResponse;
import com.zuev.gpsgateway.registry.TrackerChannelRegistry;
import com.zuev.gpsgateway.service.TrackerLoginService;
import org.springframework.stereotype.Component;

@Component
public final class CherryLoginPackageHandler extends LoginPackageHandler<CherryLoginPackage> {
    private static final byte SUCCESS_STATUS = 0;
    private static final byte UNKNOWN_IMEI_STATUS = 1;

    public CherryLoginPackageHandler(TrackerLoginService trackerLoginService,
                                     TrackerChannelRegistry trackerChannelRegistry) {
        super(CherryLoginPackage.class, trackerLoginService, trackerChannelRegistry);
    }

    @Override
    protected String getImei(CherryLoginPackage pkg) {
        return pkg.imei();
    }

    @Override
    protected String getPassword(CherryLoginPackage pkg) {
        return null;
    }

    @Override
    protected CherryLoginResponse createSuccessResponse(CherryLoginPackage pkg, Tracker tracker) {
        return new CherryLoginResponse(SUCCESS_STATUS);
    }

    @Override
    protected CherryLoginResponse createUnknownImeiResponse(CherryLoginPackage pkg) {
        return new CherryLoginResponse(UNKNOWN_IMEI_STATUS);
    }

    @Override
    protected CherryLoginResponse createWrongPasswordResponse(CherryLoginPackage pkg) {
        throw new PasswordNotSupportedException("Cherry protocol doesn't support password");
    }
}
