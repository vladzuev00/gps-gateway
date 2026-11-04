package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.exception.NoSuitablePackageHandlerException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.findImei;

@Slf4j
@RequiredArgsConstructor
public final class ProtocolHandler extends ChannelInboundHandlerAdapter {
    private static final String UNKNOWN_IMEI = "unknown";

    private final List<? extends PackageHandler<?>> packageHandlers;

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        log.info("Channel is active: {}", context.channel());
        super.channelActive(context);
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) throws Exception {
        log.info("Channel is inactive: {}, imei: {}", context.channel(), getImeiOrUnknown(context));
        super.channelInactive(context);
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object pkg) {
        log.debug("Handling package {} from {}, imei: {}", pkg, context.channel(), getImeiOrUnknown(context));
        findHandler(pkg, context).handleAsync(pkg, context);
    }

    private PackageHandler<?> findHandler(Object pkg, ChannelHandlerContext context) {
        return packageHandlers.stream()
                .filter(handler -> handler.isAbleHandle(pkg))
                .findFirst()
                .orElseThrow(
                        () -> new NoSuitablePackageHandlerException(
                                "No package handler for: %s, imei: %s".formatted(pkg, getImeiOrUnknown(context))
                        )
                );
    }

    private static String getImeiOrUnknown(ChannelHandlerContext context) {
        return findImei(context).orElse(UNKNOWN_IMEI);
    }
}
