package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoAuthPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public final class MangoAuthPackageDecoder extends MangoPackageDecoder {
    private static final byte[] PREFIX = {0x56, 0x5A, 0x01};
    private static final int IMEI_LENGTH = 15;

    public MangoAuthPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected MangoAuthPackage decodePayload(ByteBuf payload) {
        String imei = payload.readString(IMEI_LENGTH, UTF_8);
        int passwordLength = payload.readUnsignedByte();
        String password = payload.readString(passwordLength, UTF_8);
        return new MangoAuthPackage(imei, password);
    }
}
