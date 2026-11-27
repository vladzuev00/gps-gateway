package com.zuev.gpsgateway.exceptionhandler.mango;

import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public final class MangoExceptionHandlerTest {
    private final MangoExceptionHandler handler = new MangoExceptionHandler();

    @Test
    public void exceptionShouldBeHandled() {
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        RuntimeException givenException = new RuntimeException("test exception");

        handler.handle(givenContext, givenException);

        verify(givenContext).close();
    }
}
