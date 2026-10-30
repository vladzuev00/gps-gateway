//package com.zuev.gpsgateway.handler.base;
//
//import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.Reply;
//import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.ReplyAndClose;
//import com.zuev.gpsgateway.model.Tracker;
//import com.zuev.gpsgateway.model.TrackerLoginResult.Success;
//import com.zuev.gpsgateway.model.TrackerLoginResult.UnknownImei;
//import com.zuev.gpsgateway.model.TrackerLoginResult.WrongPassword;
//import com.zuev.gpsgateway.service.TrackerService;
//import io.netty.channel.ChannelHandlerContext;
//
//import java.util.concurrent.CompletableFuture;
//
//import static com.zuev.gpsgateway.util.ChannelAttributeUtil.*;
//import static com.zuev.gpsgateway.util.TrackerImeiUtil.normalizeImei;
//import static java.util.concurrent.CompletableFuture.completedFuture;
//
//public abstract class LoginPackageHandler<PKG> extends PackageHandler<PKG> {
//    private final TrackerService trackerService;
//
//    public LoginPackageHandler(Class<PKG> packageType, TrackerService trackerService) {
//        super(packageType);
//        this.trackerService = trackerService;
//    }
//
//    @Override
//    protected final CompletableFuture<HandlingResult> handleAsyncInternal(PKG pkg, ChannelHandlerContext context) {
//        String normalizedImei = normalizeImei(getImei(pkg));
//        setImei(context, normalizedImei);
//        String password = getPassword(pkg);
//        return trackerService.login(normalizedImei, password)
//                .thenCompose(
//                        result -> switch (result) {
//                            case Success(Tracker tracker) -> handleSuccess(context, tracker, pkg);
//                            case UnknownImei() -> handleUnknownImei(pkg);
//                            case WrongPassword() -> handleWrongPassword(pkg);
//                        }
//                );
//    }
//
//    protected abstract String getImei(PKG pkg);
//
//    protected abstract String getPassword(PKG pkg);
//
//    protected abstract Object createSuccessResponse(PKG pkg, Tracker tracker);
//
//    protected abstract Object createUnknownImeiResponse(PKG pkg);
//
//    protected abstract Object createWrongPasswordResponse(PKG pkg);
//
//    private CompletableFuture<HandlingResult> handleSuccess(ChannelHandlerContext context, Tracker tracker, PKG pkg) {
//        return trackerService.findLastMessage(tracker.id())
//                .thenApplyAsync(
//                        optionalLastMessage -> {
//                            setTracker(context, tracker);
//                            optionalLastMessage.ifPresent(m -> setLastMessage(context, m));
//                            //TODO подумать надо ли куда-то сохранять контект трекера, в какой-то мапе, чтобы потом трекеру отправлять команды
//                            Object response = createSuccessResponse(pkg, tracker);
//                            return new Reply(response);
//                        },
//                        context.channel().eventLoop()
//                );
//    }
//
//    private CompletableFuture<HandlingResult> handleUnknownImei(PKG pkg) {
//        Object response = createUnknownImeiResponse(pkg);
//        return completedFuture(new ReplyAndClose(response));
//    }
//
//    private CompletableFuture<HandlingResult> handleWrongPassword(PKG pkg) {
//        Object response = createWrongPasswordResponse(pkg);
//        return completedFuture(new ReplyAndClose(response));
//    }
//}
