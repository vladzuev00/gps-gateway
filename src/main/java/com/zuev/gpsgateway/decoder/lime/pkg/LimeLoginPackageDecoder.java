package com.zuev.gpsgateway.decoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeLoginPackage;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder.Tags.IMEI;
import static com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder.Tags.PASSWORD;
import static java.nio.charset.StandardCharsets.US_ASCII;

@Component
public final class LimeLoginPackageDecoder extends LimePackageDecoder {

    public LimeLoginPackageDecoder() {
        super(Set.of(IMEI, PASSWORD));
    }

    @Override
    protected LimeLoginPackage decodeValueSlices(ValueSlices slices) {
        String imei = decodeImei(slices);
        String password = decodePassword(slices);
        return new LimeLoginPackage(imei, password);
    }

    private String decodeImei(ValueSlices slices) {
        return slices.getRequired(IMEI).toString(US_ASCII);
    }

    private String decodePassword(ValueSlices slices) {
        return slices.getRequired(PASSWORD).skipBytes(PASSWORD_LENGTH_LENGTH).toString(US_ASCII);
    }
}
