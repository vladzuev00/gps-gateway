package com.zuev.gpsgateway.decoder.base;

import com.zuev.gpsgateway.decoder.base.pkg.PackageDecoder;
import io.netty.buffer.ByteBuf;

import java.util.List;

import static io.netty.buffer.ByteBufUtil.hexDump;

public abstract class BinaryProtocolDecoder extends ProtocolDecoder<ByteBuf> {

    public BinaryProtocolDecoder(List<? extends PackageDecoder<ByteBuf>> packageDecoders) {
        super(packageDecoders);
    }

    @Override
    protected final ByteBuf toPackageSource(ByteBuf byteBuf) {
        return byteBuf;
    }

    @Override
    protected final String toLogString(ByteBuf byteBuf) {
        return hexDump(byteBuf);
    }
}
