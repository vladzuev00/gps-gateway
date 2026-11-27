package com.zuev.gpsgateway.exceptionhandler.cherry;

import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public final class CherryExceptionHandlerTest {
    private final CherryExceptionHandler handler = new CherryExceptionHandler();

    @Test
    public void exceptionShouldBeHandled() {
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        RuntimeException givenException = new RuntimeException("test exception");

        handler.handle(givenContext, givenException);

        verify(givenContext).close();
    }
}
