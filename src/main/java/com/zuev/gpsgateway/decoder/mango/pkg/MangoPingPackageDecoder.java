package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoPingPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class MangoPingPackageDecoder extends MangoPackageDecoder {
    private static final byte TYPE = 0x02;

    public MangoPingPackageDecoder() {
        super(TYPE);
    }

    @Override
    protected MangoPingPackage decodePayload(ByteBuf payload) {
        return new MangoPingPackage();
    }
}
