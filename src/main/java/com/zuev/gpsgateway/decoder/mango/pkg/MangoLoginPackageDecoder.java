package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoLoginPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.US_ASCII;

@Component
public final class MangoLoginPackageDecoder extends MangoPackageDecoder {
    private static final byte[] PREFIX = {0x56, 0x5A, 0x01};
    private static final int IMEI_LENGTH = 15;

    public MangoLoginPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected MangoLoginPackage decodePayload(ByteBuf payload) {
        String imei = payload.readString(IMEI_LENGTH, US_ASCII);
        short passwordLength = payload.readUnsignedByte();
        String password = payload.readString(passwordLength, US_ASCII);
        return new MangoLoginPackage(imei, password);
    }
}
