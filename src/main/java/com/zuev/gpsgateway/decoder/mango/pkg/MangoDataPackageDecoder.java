package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoDataPackage;
import com.zuev.gpsgateway.model.mango.MangoMessage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class MangoDataPackageDecoder extends MangoPackageDecoder {
    private static final byte[] PREFIX = {0x56, 0x5A, 0x03};

    private final MangoMessageDecoder messageDecoder;

    public MangoDataPackageDecoder(MangoMessageDecoder messageDecoder) {
        super(PREFIX);
        this.messageDecoder = messageDecoder;
    }

    @Override
    protected MangoDataPackage decodePayload(ByteBuf payload) {
        MangoMessage message = messageDecoder.decode(payload);
        return new MangoDataPackage(message);
    }
}
