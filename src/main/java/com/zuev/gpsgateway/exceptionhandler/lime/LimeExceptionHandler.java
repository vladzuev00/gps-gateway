package com.zuev.gpsgateway.exceptionhandler.lime;

import com.zuev.gpsgateway.exception.NotLoggedInTrackerException;
import com.zuev.gpsgateway.exception.NoSuitableDecoderException;
import com.zuev.gpsgateway.exception.lime.LimeMissingTagException;
import com.zuev.gpsgateway.exception.lime.LimeUnknownTagException;
import com.zuev.gpsgateway.exceptionhandler.base.ExceptionHandler;
import com.zuev.gpsgateway.model.lime.LimeErrorResponse;
import io.netty.channel.ChannelHandlerContext;

import static io.netty.channel.ChannelFutureListener.CLOSE;
import static io.netty.channel.ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE;

public final class LimeExceptionHandler extends ExceptionHandler {
    private static final byte UNKNOWN_TAG_ERROR_CODE = 0x01;
    private static final byte MISSING_TAG_ERROR_CODE = 0x02;
    private static final byte NOT_LOGGED_IN_ERROR_CODE = 0x03;

    @Override
    protected void handleException(ChannelHandlerContext context, Throwable exception) {
        if (exception instanceof LimeUnknownTagException || exception instanceof NoSuitableDecoderException) {
            reply(context, UNKNOWN_TAG_ERROR_CODE);
        } else if (exception instanceof LimeMissingTagException) {
            reply(context, MISSING_TAG_ERROR_CODE);
        } else if (exception instanceof NotLoggedInTrackerException) {
            context.writeAndFlush(new LimeErrorResponse(NOT_LOGGED_IN_ERROR_CODE))
                    .addListener(FIRE_EXCEPTION_ON_FAILURE)
                    .addListener(CLOSE);
        } else {
            context.close();
        }
    }

    private void reply(ChannelHandlerContext context, byte errorCode) {
        context.writeAndFlush(new LimeErrorResponse(errorCode))
                .addListener(FIRE_EXCEPTION_ON_FAILURE);
    }
}
