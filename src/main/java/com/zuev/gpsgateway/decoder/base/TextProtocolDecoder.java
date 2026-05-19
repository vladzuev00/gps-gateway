package com.zuev.gpsgateway.decoder.base;

import com.zuev.gpsgateway.decoder.base.pkg.PackageDecoder;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.List;

public abstract class TextProtocolDecoder extends ProtocolDecoder<String> {
    private final Charset charset;

    public TextProtocolDecoder(List<? extends PackageDecoder<String>> packageDecoders, Charset charset) {
        super(packageDecoders);
        this.charset = charset;
    }

    @Override
    protected final String toPackageSource(ByteBuf byteBuf) {
        return byteBuf.toString(charset);
    }

    @Override
    protected final String toLogString(String text) {
        return text;
    }
}
