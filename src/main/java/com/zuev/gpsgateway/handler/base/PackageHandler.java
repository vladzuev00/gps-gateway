package com.zuev.gpsgateway.handler.base;

import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

import static io.netty.channel.ChannelFutureListener.CLOSE;
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
                        (result, exception) -> {
                            if (exception != null) {
                                context.fireExceptionCaught(exception);
                            } else if (result != null) {
                                result.applyTo(context);
                            }
                        },
                        context.channel().eventLoop()
                );
    }

    protected abstract CompletableFuture<HandlingResult> handleAsyncInternal(PKG pkg, ChannelHandlerContext context);

    protected sealed interface HandlingResult {
        void applyTo(ChannelHandlerContext context);

        record Reply(Object response) implements HandlingResult {

            @Override
            public void applyTo(ChannelHandlerContext context) {
                context.writeAndFlush(response).addListener(FIRE_EXCEPTION_ON_FAILURE);
            }
        }

        record Close() implements HandlingResult {

            @Override
            public void applyTo(ChannelHandlerContext context) {
                context.close();
            }
        }

        record ReplyAndClose(Object response) implements HandlingResult {

            @Override
            public void applyTo(ChannelHandlerContext context) {
                context.writeAndFlush(response).addListener(FIRE_EXCEPTION_ON_FAILURE).addListener(CLOSE);
            }
        }

        record DoNothing() implements HandlingResult {

            @Override
            public void applyTo(ChannelHandlerContext context) {
            }
        }
    }
}
