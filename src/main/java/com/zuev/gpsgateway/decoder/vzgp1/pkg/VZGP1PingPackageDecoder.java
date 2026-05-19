package com.zuev.gpsgateway.decoder.vzgp1.pkg;

import com.zuev.gpsgateway.model.vzgp1.VZGP1PingPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class VZGP1PingPackageDecoder extends VZGP1PackageDecoder {
    private static final byte[] PREFIX = {0x56, 0x5A, 0x02};

    public VZGP1PingPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected VZGP1PingPackage decodePayload(ByteBuf byteBuf) {
        return new VZGP1PingPackage();
    }
}
