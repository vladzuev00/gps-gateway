package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.exception.NoSuitablePackageHandlerException;
import com.zuev.gpsgateway.handler.base.pkg.PackageHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.findImei;

@Slf4j
@RequiredArgsConstructor
public final class ProtocolHandler extends ChannelInboundHandlerAdapter {
    private final List<? extends PackageHandler<?>> packageHandlers;

    @Override
    public void channelActive(ChannelHandlerContext context)
            throws Exception {
        log.info("Channel is active: {}", context.channel());
        super.channelActive(context);
    }

    @Override
    public void channelInactive(ChannelHandlerContext context)
            throws Exception {
        findImei(context)
                .ifPresentOrElse(
                        imei -> log.info("Channel is inactive: {}, imei: {}", context.channel(), imei),
                        () -> log.info("Channel is inactive: {}", context.channel())
                );
        super.channelInactive(context);
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object pkg) {
        findImei(context)
                .ifPresentOrElse(
                        imei -> log.debug("Handling package {} on channel {}, imei: {}", pkg, context.channel(), imei),
                        () -> log.debug("Handling package {} on channel {}", pkg, context.channel())
                );
        findHandler(pkg).handleAsync(pkg, context);
    }

    private PackageHandler<?> findHandler(Object pkg) {
        return packageHandlers.stream()
                .filter(handler -> handler.isAbleHandle(pkg))
                .findFirst()
                .orElseThrow(() -> new NoSuitablePackageHandlerException("No package handler for: " + pkg));
    }
}
