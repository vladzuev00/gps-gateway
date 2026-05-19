package com.zuev.gpsgateway.decoder.vzgp1.pkg;

import com.zuev.gpsgateway.model.vzgp1.VZGP1Data;
import com.zuev.gpsgateway.model.vzgp1.VZGP1DataPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class VZGP1DataPackageDecoder extends VZGP1PackageDecoder {
    private static final byte[] PREFIX = {0x56, 0x5A, 0x03};

    private final VZGP1DataDecoder dataDecoder;

    public VZGP1DataPackageDecoder(VZGP1DataDecoder dataDecoder) {
        super(PREFIX);
        this.dataDecoder = dataDecoder;
    }

    @Override
    protected VZGP1DataPackage decodePayload(ByteBuf byteBuf) {
        VZGP1Data data = dataDecoder.read(byteBuf);
        return new VZGP1DataPackage(data);
    }
}
