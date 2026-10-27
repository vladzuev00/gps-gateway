package com.zuev.gpsgateway.handler.base;

import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

import static io.netty.channel.ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE;

@RequiredArgsConstructor
public abstract class PackageHandler<REQUEST> {
    private final Class<REQUEST> requestType;

    public final boolean isAbleHandle(Object request) {
        return requestType.isInstance(request);
    }

    public final void handleAsync(Object request, ChannelHandlerContext context) {
        REQUEST castedRequest = requestType.cast(request);
        handleAsyncInternal(castedRequest, context)
                .whenCompleteAsync(
                        (response, exception) -> {
                            if (exception != null) {
                                context.fireExceptionCaught(exception);
                            } else if (response != null) {
                                context.writeAndFlush(response).addListener(FIRE_EXCEPTION_ON_FAILURE);
                            }
                        },
                        context.channel().eventLoop()
                );
    }

    protected abstract CompletableFuture<?> handleAsyncInternal(REQUEST request, ChannelHandlerContext context);
}
