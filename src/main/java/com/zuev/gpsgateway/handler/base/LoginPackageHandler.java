package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.util.ChannelAttributeUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

//TODO refactor and test
public abstract class LoginPackageHandler<REQUEST> extends PackageHandler<REQUEST> {

    public LoginPackageHandler(Class<REQUEST> requestType) {
        super(requestType);
    }

    @Override
    protected final CompletableFuture<?> handleAsyncInternal(REQUEST request, ChannelHandlerContext context) {
        return login(request)
                .thenApplyAsync(
                        tracker -> tracker
                                .map(loggedInTracker -> {
                                            ChannelAttributeUtil.setTracker(context, loggedInTracker);
                                            return createSuccessResponse(request);
                                        }
                                )
                                .orElseGet(() -> createFailureResponse(request)),
                        context.channel().eventLoop()
                );
    }

    protected abstract CompletableFuture<Optional<Tracker>> login(REQUEST request);

    protected abstract Object createSuccessResponse(REQUEST request);

    protected abstract Object createFailureResponse(REQUEST request);
}
