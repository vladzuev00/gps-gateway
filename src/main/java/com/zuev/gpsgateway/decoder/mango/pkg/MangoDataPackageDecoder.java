package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoData;
import com.zuev.gpsgateway.model.mango.MangoDataPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class MangoDataPackageDecoder extends MangoPackageDecoder {
    private static final byte[] PREFIX = {0x56, 0x5A, 0x03};

    private final MangoDataDecoder dataDecoder;

    public MangoDataPackageDecoder(MangoDataDecoder dataDecoder) {
        super(PREFIX);
        this.dataDecoder = dataDecoder;
    }

    @Override
    protected MangoDataPackage decodePayload(ByteBuf payload) {
        MangoData data = dataDecoder.read(payload);
        return new MangoDataPackage(data);
    }
}
