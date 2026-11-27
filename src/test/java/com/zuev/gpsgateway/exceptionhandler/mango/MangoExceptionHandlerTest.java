package com.zuev.gpsgateway.exceptionhandler.mango;

import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public final class MangoExceptionHandlerTest {
    private final MangoExceptionHandler handler = new MangoExceptionHandler();

    @Test
    public void exceptionShouldBeHandled() {
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        Throwable givenException = mock(Throwable.class);

        handler.handle(givenContext, givenException);

        verify(givenContext).close();
        verifyNoInteractions(givenException);
    }
}
