package com.zuev.gpsgateway.handler.base;

import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PackageHandler<REQUEST> {
    private final Class<REQUEST> requestType;

    public final boolean isAbleHandle(Object request) {
        return requestType.isInstance(request);
    }

    public final void handle(Object request, ChannelHandlerContext context) {
        REQUEST castedRequest = requestType.cast(request);
        Object response = handleInternal(castedRequest, context);
        context.writeAndFlush(response);
    }

    protected abstract Object handleInternal(REQUEST request, ChannelHandlerContext context);
}
