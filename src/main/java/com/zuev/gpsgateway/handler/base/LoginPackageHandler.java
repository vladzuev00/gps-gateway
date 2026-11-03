package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.DoNothing;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.Reply;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.ReplyAndClose;
import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.model.TrackerLoginResult.Success;
import com.zuev.gpsgateway.model.TrackerLoginResult.UnknownImei;
import com.zuev.gpsgateway.model.TrackerLoginResult.WrongPassword;
import com.zuev.gpsgateway.service.TrackerChannelRegistry;
import com.zuev.gpsgateway.service.TrackerLoginService;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.CompletableFuture;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.setImei;
import static com.zuev.gpsgateway.util.ChannelAttributeUtil.setTracker;

public abstract class LoginPackageHandler<PKG> extends PackageHandler<PKG> {
    private final TrackerLoginService trackerLoginService;
    private final TrackerChannelRegistry trackerChannelRegistry;

    public LoginPackageHandler(Class<PKG> packageType,
                               TrackerLoginService trackerLoginService,
                               TrackerChannelRegistry trackerChannelRegistry) {
        super(packageType);
        this.trackerLoginService = trackerLoginService;
        this.trackerChannelRegistry = trackerChannelRegistry;
    }

    @Override
    protected final CompletableFuture<HandlingResult> handleAsyncInternal(PKG pkg, ChannelHandlerContext context) {
        String imei = getImei(pkg);
        setImei(context, imei);
        String password = getPassword(pkg);
        return trackerLoginService.loginAsync(imei, password)
                .thenApplyAsync(
                        result -> {
                            if (!context.channel().isActive()) {
                                return new DoNothing();
                            }
                            return switch (result) {
                                case Success(Tracker tracker) -> handleSuccess(pkg, tracker, context);
                                case UnknownImei() -> handleUnknownImei(pkg);
                                case WrongPassword() -> handleWrongPassword(pkg);
                            };
                        },
                        context.channel().eventLoop()
                );
    }

    protected abstract String getImei(PKG pkg);

    protected abstract String getPassword(PKG pkg);

    protected abstract Object createSuccessResponse(PKG pkg, Tracker tracker);

    protected abstract Object createUnknownImeiResponse(PKG pkg);

    protected abstract Object createWrongPasswordResponse(PKG pkg);

    private HandlingResult handleSuccess(PKG pkg, Tracker tracker, ChannelHandlerContext context) {
        setTracker(context, tracker);
        trackerChannelRegistry.register(tracker.id(), context.channel());
        Object response = createSuccessResponse(pkg, tracker);
        return new Reply(response);
    }

    private HandlingResult handleUnknownImei(PKG pkg) {
        Object response = createUnknownImeiResponse(pkg);
        return new ReplyAndClose(response);
    }

    private HandlingResult handleWrongPassword(PKG pkg) {
        Object response = createWrongPasswordResponse(pkg);
        return new ReplyAndClose(response);
    }
}
