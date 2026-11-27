package com.zuev.gpsgateway.exceptionhandler.cherry;

import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public final class CherryExceptionHandlerTest {
    private final CherryExceptionHandler handler = new CherryExceptionHandler();

    @Test
    public void exceptionShouldBeHandled() {
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        Throwable givenException = mock(Throwable.class);

        handler.handle(givenContext, givenException);

        verify(givenContext).close();
        verifyNoInteractions(givenException);
    }
}
