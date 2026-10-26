package com.zuev.gpsgateway.handler.base;

import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public abstract class PackageHandler<REQUEST> {
    private final Class<REQUEST> requestType;

    public final boolean isAbleHandle(Object request) {
        return requestType.isInstance(request);
    }

    public final void handle(Object request, ChannelHandlerContext context) {
        REQUEST castedRequest = requestType.cast(request);
        handleInternal(castedRequest, context)
                .whenComplete(
                        (response, exception) -> {
                            if (exception != null) {
                                context.fireExceptionCaught(exception);
                            } else {
                                context.writeAndFlush(response);
                            }
                        }
                );
    }

    protected abstract CompletableFuture<?> handleInternal(REQUEST request, ChannelHandlerContext context);
}
