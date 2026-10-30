package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.DoNothing;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.Reply;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.ReplyAndClose;
import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.model.TrackerLoginResult.Success;
import com.zuev.gpsgateway.model.TrackerLoginResult.UnknownImei;
import com.zuev.gpsgateway.model.TrackerLoginResult.WrongPassword;
import com.zuev.gpsgateway.service.MessageService;
import com.zuev.gpsgateway.service.TrackerChannelRegistry;
import com.zuev.gpsgateway.service.TrackerLoginService;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.CompletableFuture;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.*;
import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class LoginPackageHandler<PKG> extends PackageHandler<PKG> {
    private final TrackerLoginService trackerLoginService;
    private final MessageService messageService;
    private final TrackerChannelRegistry trackerChannelRegistry;

    public LoginPackageHandler(Class<PKG> packageType,
                               TrackerLoginService trackerLoginService,
                               MessageService messageService,
                               TrackerChannelRegistry trackerChannelRegistry) {
        super(packageType);
        this.trackerLoginService = trackerLoginService;
        this.messageService = messageService;
        this.trackerChannelRegistry = trackerChannelRegistry;
    }

    @Override
    protected final CompletableFuture<HandlingResult> handleAsyncInternal(PKG pkg, ChannelHandlerContext context) {
        String imei = getImei(pkg);
        setImei(context, imei);
        String password = getPassword(pkg);
        return trackerLoginService.loginAsync(imei, password)
                .thenComposeAsync(
                        result -> switch (result) {
                            case Success(Tracker tracker) -> handleSuccessAsync(pkg, tracker, context);
                            case UnknownImei() -> completedFuture(handleUnknownImei(pkg));
                            case WrongPassword() -> completedFuture(handleWrongPassword(pkg));
                        },
                        context.channel().eventLoop()
                );
    }

    protected abstract String getImei(PKG pkg);

    protected abstract String getPassword(PKG pkg);

    protected abstract Object createSuccessResponse(PKG pkg, Tracker tracker);

    protected abstract Object createUnknownImeiResponse(PKG pkg);

    protected abstract Object createWrongPasswordResponse(PKG pkg);

    private CompletableFuture<HandlingResult> handleSuccessAsync(PKG pkg, Tracker tracker, ChannelHandlerContext context) {
        return messageService.findLastMessageAsync(tracker.id())
                .thenApplyAsync(
                        optionalMessage -> {
                            if (!context.channel().isActive()) {
                                return new DoNothing();
                            }
                            optionalMessage.ifPresent(m -> setLastMessage(context, m));
                            setTracker(context, tracker);
                            trackerChannelRegistry.register(tracker.id(), context.channel());
                            Object response = createSuccessResponse(pkg, tracker);
                            return new Reply(response);
                        },
                        context.channel().eventLoop()
                );
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
