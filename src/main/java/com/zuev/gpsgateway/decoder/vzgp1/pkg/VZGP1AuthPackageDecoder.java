package com.zuev.gpsgateway.decoder.vzgp1.pkg;

import com.zuev.gpsgateway.model.vzgp1.VZGP1AuthPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public final class VZGP1AuthPackageDecoder extends VZGP1PackageDecoder {
    private static final byte[] PREFIX = {0x56, 0x5A, 0x01};
    private static final int IMEI_LENGTH = 15;

    public VZGP1AuthPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected VZGP1AuthPackage decodePayload(ByteBuf byteBuf) {
        String imei = byteBuf.readString(IMEI_LENGTH, UTF_8);
        int passwordLength = byteBuf.readUnsignedByte();
        String password = byteBuf.readString(passwordLength, UTF_8);
        return new VZGP1AuthPackage(imei, password);
    }
}
