package com.zuev.gpsgateway.decoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeLoginPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import java.util.Map;
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
    protected LimeLoginPackage decodeValueSlices(Map<Byte, ByteBuf> slicesByTags) {
        String imei = decodeImei(slicesByTags);
        String password = decodePassword(slicesByTags);
        return new LimeLoginPackage(imei, password);
    }

    private String decodeImei(Map<Byte, ByteBuf> slicesByTags) {
        return slicesByTags.get(IMEI).toString(US_ASCII);
    }

    private String decodePassword(Map<Byte, ByteBuf> slicesByTags) {
        return slicesByTags.get(PASSWORD).skipBytes(PASSWORD_LENGTH_LENGTH).toString(US_ASCII);
    }
}
