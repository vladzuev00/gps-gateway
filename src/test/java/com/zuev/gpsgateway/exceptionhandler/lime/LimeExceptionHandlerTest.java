package com.zuev.gpsgateway.exceptionhandler.lime;

import com.zuev.gpsgateway.exception.NotLoggedInTrackerException;
import com.zuev.gpsgateway.exception.NoSuitableDecoderException;
import com.zuev.gpsgateway.exception.lime.LimeMissingTagException;
import com.zuev.gpsgateway.exception.lime.LimeUnknownTagException;
import com.zuev.gpsgateway.model.lime.LimeErrorResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.Test;

import static io.netty.channel.ChannelFutureListener.CLOSE;
import static io.netty.channel.ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE;
import static org.mockito.Mockito.*;

public final class LimeExceptionHandlerTest {
    private final LimeExceptionHandler handler = new LimeExceptionHandler();

    @Test
    public void unknownTagExceptionShouldBeHandled() {
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        ChannelFuture givenWriteFuture = mock(ChannelFuture.class);
        when(givenContext.writeAndFlush(new LimeErrorResponse((byte) 0x01))).thenReturn(givenWriteFuture);
        LimeUnknownTagException givenException = new LimeUnknownTagException("unknown tag");

        handler.handle(givenContext, givenException);

        verify(givenWriteFuture).addListener(FIRE_EXCEPTION_ON_FAILURE);
        verify(givenContext, never()).close();
    }

    @Test
    public void noSuitableDecoderExceptionShouldBeHandled() {
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        ChannelFuture givenWriteFuture = mock(ChannelFuture.class);
        when(givenContext.writeAndFlush(new LimeErrorResponse((byte) 0x01))).thenReturn(givenWriteFuture);
        NoSuitableDecoderException givenException = new NoSuitableDecoderException("no suitable decoder");

        handler.handle(givenContext, givenException);

        verify(givenWriteFuture).addListener(FIRE_EXCEPTION_ON_FAILURE);
        verify(givenContext, never()).close();
    }

    @Test
    public void missingTagExceptionShouldBeHandled() {
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        ChannelFuture givenWriteFuture = mock(ChannelFuture.class);
        when(givenContext.writeAndFlush(new LimeErrorResponse((byte) 0x02))).thenReturn(givenWriteFuture);
        LimeMissingTagException givenException = new LimeMissingTagException("missing tag");

        handler.handle(givenContext, givenException);

        verify(givenWriteFuture).addListener(FIRE_EXCEPTION_ON_FAILURE);
        verify(givenContext, never()).close();
    }

    @Test
    public void notLoggedInTrackerExceptionShouldBeHandled() {
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        ChannelFuture givenWriteFuture = mock(ChannelFuture.class, RETURNS_SELF);
        when(givenContext.writeAndFlush(new LimeErrorResponse((byte) 0x03))).thenReturn(givenWriteFuture);
        NotLoggedInTrackerException givenException = new NotLoggedInTrackerException();

        handler.handle(givenContext, givenException);

        verify(givenWriteFuture).addListener(FIRE_EXCEPTION_ON_FAILURE);
        verify(givenWriteFuture).addListener(CLOSE);
    }

    @Test
    public void unknownExceptionShouldBeHandled() {
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        RuntimeException givenException = new RuntimeException("test exception");

        handler.handle(givenContext, givenException);

        verify(givenContext).close();
        verify(givenContext, never()).writeAndFlush(any());
    }
}
