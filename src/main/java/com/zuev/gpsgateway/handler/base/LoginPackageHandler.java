//package com.zuev.gpsgateway.handler.base;
//
//import com.zuev.gpsgateway.model.Tracker;
//import com.zuev.gpsgateway.model.TrackerLoginResult;
//import com.zuev.gpsgateway.model.TrackerLoginResult.Success;
//import com.zuev.gpsgateway.model.TrackerLoginResult.UnknownImei;
//import com.zuev.gpsgateway.model.TrackerLoginResult.WrongPassword;
//import com.zuev.gpsgateway.service.TrackerLoginService;
//import com.zuev.gpsgateway.util.ChannelAttributeUtil;
//import io.netty.channel.ChannelHandlerContext;
//
//import java.util.concurrent.CompletableFuture;
//
////TODO refactor and test
//public abstract class LoginPackageHandler<REQUEST> extends PackageHandler<REQUEST> {
//    private final TrackerLoginService trackerLoginService;
//
//    public LoginPackageHandler(Class<REQUEST> requestType, TrackerLoginService trackerLoginService) {
//        super(requestType);
//        this.trackerLoginService = trackerLoginService;
//    }
//
//    @Override
//    protected final CompletableFuture<?> handleAsyncInternal(REQUEST request, ChannelHandlerContext context) {
//        return trackerLoginService.login()
//                .thenApplyAsync(
//                        result -> switch (result) {
//                            case Success(Tracker tracker) -> {
//                                ChannelAttributeUtil.setTracker(context, tracker);
//                                yield createSuccessResponse(request);
//                            }
//                            case UnknownImei() -> createUnknownImeiResponse(request);
//                            case WrongPassword() -> createWrongPasswordResponse(request);
//                        },
//                        context.channel().eventLoop()
//                );
//    }
//
//    protected abstract String getImei(REQUEST request);
//
//
//
//    protected abstract Object createSuccessResponse(REQUEST request);
//
//    protected abstract Object createUnknownImeiResponse(REQUEST request);
//
//    protected abstract Object createWrongPasswordResponse(REQUEST request);
//}
