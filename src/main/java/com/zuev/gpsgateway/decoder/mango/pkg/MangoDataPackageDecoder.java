package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoDataPackage;
import com.zuev.gpsgateway.model.mango.MangoMessage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class MangoDataPackageDecoder extends MangoPackageDecoder {
    private static final byte TYPE = 0x03;

    private final MangoMessageDecoder messageDecoder;

    public MangoDataPackageDecoder(MangoMessageDecoder messageDecoder) {
        super(TYPE);
        this.messageDecoder = messageDecoder;
    }

    @Override
    protected MangoDataPackage decodePayload(ByteBuf payload) {
        MangoMessage message = messageDecoder.decode(payload);
        return new MangoDataPackage(message);
    }
}
