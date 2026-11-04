package com.zuev.gpsgateway.handler.base.pkg;

import com.zuev.gpsgateway.handler.base.pkg.PackageHandler.HandlingResult.Reply;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class PingPackageHandler<PKG> extends PackageHandler<PKG> {

    public PingPackageHandler(Class<PKG> packageType) {
        super(packageType);
    }

    @Override
    protected final CompletableFuture<HandlingResult> handleAsyncInternal(PKG pkg, ChannelHandlerContext context) {
        Object response = createResponse(pkg);
        return completedFuture(new Reply(response));
    }

    protected abstract Object createResponse(PKG pkg);
}
