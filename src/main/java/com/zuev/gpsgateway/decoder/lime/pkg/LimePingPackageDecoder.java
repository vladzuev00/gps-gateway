package com.zuev.gpsgateway.decoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimePingPackage;
import org.springframework.stereotype.Component;

import static java.util.Collections.emptySet;

@Component
public final class LimePingPackageDecoder extends LimePackageDecoder {

    public LimePingPackageDecoder() {
        super(emptySet());
    }

    @Override
    protected LimePingPackage decodeValueSlices(ValueSlices slices) {
        return new LimePingPackage();
    }
}
