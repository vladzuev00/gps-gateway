package com.zuev.gpsgateway.exceptionhandler.base;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import lombok.extern.slf4j.Slf4j;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.findImei;

@Slf4j
public abstract class ExceptionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public final void exceptionCaught(ChannelHandlerContext context, Throwable exception) {
        findImei(context)
                .ifPresentOrElse(
                        imei -> log.error("Exception caught on channel {}, imei: {}", context.channel(), imei, exception),
                        () -> log.error("Exception caught on channel {}", context.channel(), exception)
                );
        handle(context, getDecoderExceptionCauseOrSelf(exception));
    }

    protected abstract void handle(ChannelHandlerContext context, Throwable exception);

    private Throwable getDecoderExceptionCauseOrSelf(Throwable exception) {
        return exception instanceof DecoderException decoderException && decoderException.getCause() != null
                ? decoderException.getCause()
                : exception;
    }
}
