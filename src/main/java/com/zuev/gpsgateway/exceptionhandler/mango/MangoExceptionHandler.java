package com.zuev.gpsgateway.exceptionhandler.mango;

import com.zuev.gpsgateway.exceptionhandler.base.ExceptionHandler;
import io.netty.channel.ChannelHandlerContext;

public final class MangoExceptionHandler extends ExceptionHandler {

    @Override
    protected void handle(ChannelHandlerContext context, Throwable exception) {
        context.close();
    }
}
