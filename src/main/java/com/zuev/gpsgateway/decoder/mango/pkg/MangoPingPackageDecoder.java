package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoPingPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class MangoPingPackageDecoder extends MangoPackageDecoder {
    private static final byte[] PREFIX = {0x56, 0x5A, 0x02};

    public MangoPingPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected MangoPingPackage decodePayload(ByteBuf payload) {
        return new MangoPingPackage();
    }
}
