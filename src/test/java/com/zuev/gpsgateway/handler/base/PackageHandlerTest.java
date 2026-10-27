package com.zuev.gpsgateway.handler.base;

import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.failedFuture;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public final class PackageHandlerTest {
    private final TestPackageHandler handler = new TestPackageHandler();

    @Test
    public void handlerShouldBeAbleToHandleRequest() {
        TestPackage givenRequest = new TestPackage(5);

        assertTrue(handler.isAbleHandle(givenRequest));
    }

    @Test
    public void handlerShouldNotBeAbleToHandleRequest() {
        Object givenRequest = new Object();

        assertFalse(handler.isAbleHandle(givenRequest));
    }

    @Test
    public void requestShouldBeHandledWithResponse() {
        EmbeddedChannel givenChannel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
        TestPackage givenRequest = new TestPackage(5);
        ChannelHandlerContext givenContext = givenChannel.pipeline().firstContext();

        handler.handleAsync(givenRequest, givenContext);
        givenChannel.runPendingTasks();

        assertDoesNotThrow(givenChannel::checkException);
        TestPackage actual = givenChannel.readOutbound();
        TestPackage expected = new TestPackage(6);
        assertEquals(expected, actual);
    }

    @Test
    public void requestShouldBeHandledWithoutResponse() {
        EmbeddedChannel givenChannel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
        TestPackage givenRequest = new TestPackage(5);
        ChannelHandlerContext givenContext = spy(givenChannel.pipeline().firstContext());
        PackageHandler<?> givenHandler = new PackageHandler<>(TestPackage.class) {

            @Override
            protected CompletableFuture<?> handleAsyncInternal(TestPackage request, ChannelHandlerContext context) {
                return completedFuture(null);
            }
        };

        givenHandler.handleAsync(givenRequest, givenContext);
        givenChannel.runPendingTasks();

        assertDoesNotThrow(givenChannel::checkException);
        assertNull(givenChannel.readOutbound());
        verify(givenContext, never()).writeAndFlush(any());
    }

    @Test
    public void requestShouldNotBeHandledBecauseOfNotSuitableRequestType() {
        Object givenRequest = new Object();
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        assertThrows(ClassCastException.class, () -> handler.handleAsync(givenRequest, givenContext));
        verifyNoInteractions(givenContext);
    }

    @Test
    public void requestShouldNotBeHandledBecauseOfHandlingException() {
        EmbeddedChannel givenChannel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
        TestPackage givenRequest = new TestPackage(5);
        ChannelHandlerContext givenContext = spy(givenChannel.pipeline().firstContext());
        RuntimeException givenException = new RuntimeException();
        PackageHandler<?> givenHandler = new PackageHandler<>(TestPackage.class) {

            @Override
            protected CompletableFuture<?> handleAsyncInternal(TestPackage request, ChannelHandlerContext context) {
                return failedFuture(givenException);
            }
        };

        givenHandler.handleAsync(givenRequest, givenContext);
        givenChannel.runPendingTasks();

        RuntimeException actual = assertThrows(RuntimeException.class, givenChannel::checkException);
        assertSame(givenException, actual);
        verify(givenContext, never()).writeAndFlush(any());
    }

    @Test
    public void writingResponseExceptionShouldBeFiredToPipeline() {
        RuntimeException givenException = new RuntimeException();
        EmbeddedChannel givenChannel = new EmbeddedChannel(
                new ChannelOutboundHandlerAdapter() {

                    @Override
                    public void write(ChannelHandlerContext context, Object message, ChannelPromise promise) {
                        throw givenException;
                    }
                },
                new ChannelInboundHandlerAdapter()
        );
        TestPackage givenRequest = new TestPackage(5);
        ChannelHandlerContext givenContext = givenChannel.pipeline().lastContext();

        handler.handleAsync(givenRequest, givenContext);
        givenChannel.runPendingTasks();

        RuntimeException actual = assertThrows(RuntimeException.class, givenChannel::checkException);
        assertSame(givenException, actual);
    }

    @Test
    public void responseCallbackShouldBeHandledByEventLoopThread() {
        TestPackage givenRequest = new TestPackage(5);
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        EventLoop givenEventLoop = mock(EventLoop.class);
        when(givenChannel.eventLoop()).thenReturn(givenEventLoop);

        handler.handleAsync(givenRequest, givenContext);

        verify(givenEventLoop).execute(any());
    }

    private record TestPackage(int value) {
    }

    private static final class TestPackageHandler extends PackageHandler<TestPackage> {

        public TestPackageHandler() {
            super(TestPackage.class);
        }

        @Override
        protected CompletableFuture<TestPackage> handleAsyncInternal(TestPackage request, ChannelHandlerContext context) {
            return completedFuture(new TestPackage(request.value() + 1));
        }
    }
}
