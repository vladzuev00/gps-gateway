package com.zuev.gpsgateway.exceptionhandler.cherry;

import com.zuev.gpsgateway.exceptionhandler.base.ExceptionHandler;
import io.netty.channel.ChannelHandlerContext;

public final class CherryExceptionHandler extends ExceptionHandler {

    @Override
    protected void handle(ChannelHandlerContext context, Throwable exception) {
        context.close();
    }
}
