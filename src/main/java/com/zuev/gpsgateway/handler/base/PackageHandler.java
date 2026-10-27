package com.zuev.gpsgateway.handler.base;

import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

import static io.netty.channel.ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE;

@RequiredArgsConstructor
public abstract class PackageHandler<PKG> {
    private final Class<PKG> packageType;

    public final boolean isAbleHandle(Object pkg) {
        return packageType.isInstance(pkg);
    }

    public final void handleAsync(Object pkg, ChannelHandlerContext context) {
        PKG castedPackage = packageType.cast(pkg);
        handleAsyncInternal(castedPackage, context)
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

    protected abstract CompletableFuture<?> handleAsyncInternal(PKG pkg, ChannelHandlerContext context);
}
