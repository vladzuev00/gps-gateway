package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.model.TrackerLoginResult.Success;
import com.zuev.gpsgateway.model.TrackerLoginResult.UnknownImei;
import com.zuev.gpsgateway.model.TrackerLoginResult.WrongPassword;
import com.zuev.gpsgateway.service.TrackerLoginService;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.CompletableFuture;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.setImei;
import static com.zuev.gpsgateway.util.TrackerImeiUtil.normalizeImei;

public abstract class LoginPackageHandler<PKG> extends PackageHandler<PKG> {
    private final TrackerLoginService loginService;

    public LoginPackageHandler(Class<PKG> packageType, TrackerLoginService loginService) {
        super(packageType);
        this.loginService = loginService;
    }

    @Override
    protected final CompletableFuture<?> handleAsyncInternal(PKG pkg, ChannelHandlerContext context) {
        String normalizedImei = normalizeImei(getImei(pkg));
        setImei(context, normalizedImei);
        String password = getPassword(pkg);
        return loginService.login(normalizedImei, password)
                .thenApplyAsync(
                        result -> switch (result) {
                            case Success(Tracker tracker) -> {
                                onSuccess(context, tracker);
                                yield createSuccessResponse(pkg);
                            }
                            //TODO После UnknownImei или WrongPassword канал остаётся открытым.
                            case UnknownImei() -> createUnknownImeiResponse(pkg);
                            case WrongPassword() -> createWrongPasswordResponse(pkg);
                        },
                        context.channel().eventLoop()
                );
    }

    protected abstract String getImei(PKG pkg);

    protected abstract String getPassword(PKG pkg);

    //TODO подумать нужен ли pkg, TrackerLoginResult
    protected abstract Object createSuccessResponse(PKG pkg);

    //TODO подумать нужен ли pkg, TrackerLoginResult
    protected abstract Object createUnknownImeiResponse(PKG pkg);

    //TODO подумать нужен ли pkg, TrackerLoginResult
    protected abstract Object createWrongPasswordResponse(PKG pkg);

    private void onSuccess(ChannelHandlerContext context, Tracker tracker) {
        //TODO надо положить в атрибут канала объект трекера
        //TODO надо положить в атрибут канала объект последнего сообщения. тоже скорее всего берется из другого сервиса
        //TODO подумать надо ли куда-то сохранять контект трекера, в какой-то мапе, чтобы потом трекеру отправлять команды
    }
}
