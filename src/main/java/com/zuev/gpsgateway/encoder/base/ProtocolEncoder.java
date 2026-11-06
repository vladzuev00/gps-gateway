package com.zuev.gpsgateway.encoder.base;

import com.zuev.gpsgateway.encoder.base.pkg.PackageEncoder;
import com.zuev.gpsgateway.exception.NoSuitableEncoderException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.findImei;

@Slf4j
@RequiredArgsConstructor
public final class ProtocolEncoder extends MessageToByteEncoder<Object> {
    private final List<? extends PackageEncoder<?>> packageEncoders;

    @Override
    protected void encode(ChannelHandlerContext context, Object pkg, ByteBuf out) {
        logEncoding(context, pkg);
        byte[] bytes = findEncoder(pkg).encode(pkg);
        out.writeBytes(bytes);
    }

    private void logEncoding(ChannelHandlerContext context, Object pkg) {
        findImei(context)
                .ifPresentOrElse(
                        imei -> log.debug("Encoding package {} on channel {}, imei: {}", pkg, context.channel(), imei),
                        () -> log.debug("Encoding package {} on channel {}", pkg, context.channel())
                );
    }

    private PackageEncoder<?> findEncoder(Object pkg) {
        return packageEncoders.stream()
                .filter(encoder -> encoder.isAbleEncode(pkg))
                .findFirst()
                .orElseThrow(() -> new NoSuitableEncoderException("No package encoder for: " + pkg));
    }
}
