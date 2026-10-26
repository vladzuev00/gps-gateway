package com.zuev.gpsgateway.handler.base;

import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public final class PackageHandlerTest {
    private final TestPackageHandler handler = new TestPackageHandler();

    @Test
    public void handlerShouldBeAbleToHandleRequest() {
        TestPackage givenRequest = new TestPackage(Integer.MAX_VALUE);

        assertTrue(handler.isAbleHandle(givenRequest));
    }

    @Test
    public void handlerShouldNotBeAbleToHandleRequest() {
        Object givenRequest = new Object();

        assertFalse(handler.isAbleHandle(givenRequest));
    }

    @Test
    public void requestShouldBeHandled() {
        TestPackage givenRequest = new TestPackage(5);
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        handler.handle(givenRequest, givenContext);

        verify(givenContext).writeAndFlush(new TestPackage(6));
    }

    @Test
    public void requestShouldNotBeHandled() {
        Object givenRequest = new Object();
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        assertThrows(ClassCastException.class, () -> handler.handle(givenRequest, givenContext));

        verifyNoInteractions(givenContext);
    }

    private record TestPackage(int value) {
    }

    private static final class TestPackageHandler extends PackageHandler<TestPackage> {

        public TestPackageHandler() {
            super(TestPackage.class);
        }

        @Override
        protected TestPackage handleInternal(TestPackage request, ChannelHandlerContext context) {
            return new TestPackage(request.value + 1);
        }
    }
}
