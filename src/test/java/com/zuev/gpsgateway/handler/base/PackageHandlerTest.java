package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.Close;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.DoNothing;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.Reply;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.ReplyAndClose;
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
    public void handlerShouldBeAbleToHandlePackage() {
        TestPackage givenPackage = new TestPackage(5);

        assertTrue(handler.isAbleHandle(givenPackage));
    }

    @Test
    public void handlerShouldNotBeAbleToHandlePackage() {
        Object givenPackage = new Object();

        assertFalse(handler.isAbleHandle(givenPackage));
    }

    @Test
    public void packageShouldBeHandledWithReply() {
        EmbeddedChannel givenChannel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
        TestPackage givenPackage = new TestPackage(5);
        ChannelHandlerContext givenContext = givenChannel.pipeline().firstContext();

        handler.handleAsync(givenPackage, givenContext);
        givenChannel.runPendingTasks();

        assertDoesNotThrow(givenChannel::checkException);
        TestPackage actual = givenChannel.readOutbound();
        TestPackage expected = new TestPackage(6);
        assertEquals(expected, actual);
        assertNull(givenChannel.readOutbound());
        assertTrue(givenChannel.isOpen());
    }

    @Test
    public void packageShouldBeHandledWithClose() {
        EmbeddedChannel givenChannel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
        TestPackage givenPackage = new TestPackage(5);
        ChannelHandlerContext givenContext = givenChannel.pipeline().firstContext();
        PackageHandler<?> givenHandler = new PackageHandler<>(TestPackage.class) {

            @Override
            protected CompletableFuture<HandlingResult> handleAsyncInternal(TestPackage pkg, ChannelHandlerContext context) {
                return completedFuture(new Close());
            }
        };

        givenHandler.handleAsync(givenPackage, givenContext);
        givenChannel.runPendingTasks();

        assertDoesNotThrow(givenChannel::checkException);
        assertNull(givenChannel.readOutbound());
        assertFalse(givenChannel.isOpen());
    }

    @Test
    public void packageShouldBeHandledWithReplyAndClose() {
        EmbeddedChannel givenChannel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
        TestPackage givenPackage = new TestPackage(5);
        ChannelHandlerContext givenContext = givenChannel.pipeline().firstContext();
        PackageHandler<?> givenHandler = new PackageHandler<>(TestPackage.class) {

            @Override
            protected CompletableFuture<HandlingResult> handleAsyncInternal(TestPackage pkg, ChannelHandlerContext context) {
                return completedFuture(new ReplyAndClose(new TestPackage(pkg.value() + 1)));
            }
        };

        givenHandler.handleAsync(givenPackage, givenContext);
        givenChannel.runPendingTasks();

        assertDoesNotThrow(givenChannel::checkException);
        TestPackage actual = givenChannel.readOutbound();
        TestPackage expected = new TestPackage(6);
        assertEquals(expected, actual);
        assertNull(givenChannel.readOutbound());
        assertFalse(givenChannel.isOpen());
    }

    @Test
    public void packageShouldBeHandledWithDoNothing() {
        EmbeddedChannel givenChannel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
        TestPackage givenPackage = new TestPackage(5);
        ChannelHandlerContext givenContext = givenChannel.pipeline().firstContext();
        PackageHandler<?> givenHandler = new PackageHandler<>(TestPackage.class) {

            @Override
            protected CompletableFuture<HandlingResult> handleAsyncInternal(TestPackage pkg, ChannelHandlerContext context) {
                return completedFuture(new DoNothing());
            }
        };

        givenHandler.handleAsync(givenPackage, givenContext);
        givenChannel.runPendingTasks();

        assertDoesNotThrow(givenChannel::checkException);
        assertNull(givenChannel.readOutbound());
        assertTrue(givenChannel.isOpen());
    }

    @Test
    public void packageShouldNotBeHandledBecauseOfNotSuitablePackageType() {
        Object givenPackage = new Object();
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        assertThrows(ClassCastException.class, () -> handler.handleAsync(givenPackage, givenContext));
        verifyNoInteractions(givenContext);
    }

    @Test
    public void packageShouldNotBeHandledBecauseOfHandlingException() {
        EmbeddedChannel givenChannel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
        TestPackage givenPackage = new TestPackage(5);
        ChannelHandlerContext givenContext = givenChannel.pipeline().firstContext();
        RuntimeException givenException = new RuntimeException();
        PackageHandler<?> givenHandler = new PackageHandler<>(TestPackage.class) {

            @Override
            protected CompletableFuture<HandlingResult> handleAsyncInternal(TestPackage pkg, ChannelHandlerContext context) {
                return failedFuture(givenException);
            }
        };

        givenHandler.handleAsync(givenPackage, givenContext);
        givenChannel.runPendingTasks();

        RuntimeException actual = assertThrows(RuntimeException.class, givenChannel::checkException);
        assertSame(givenException, actual);
        assertNull(givenChannel.readOutbound());
        assertTrue(givenChannel.isOpen());
    }

    @Test
    public void replyWritingExceptionShouldBeFiredToPipeline() {
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
        TestPackage givenPackage = new TestPackage(5);
        ChannelHandlerContext givenContext = givenChannel.pipeline().lastContext();

        handler.handleAsync(givenPackage, givenContext);
        givenChannel.runPendingTasks();

        RuntimeException actual = assertThrows(RuntimeException.class, givenChannel::checkException);
        assertSame(givenException, actual);
        assertNull(givenChannel.readOutbound());
        assertTrue(givenChannel.isOpen());
    }

    @Test
    public void replyAndCloseWritingExceptionShouldBeFiredToPipeline() {
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
        TestPackage givenPackage = new TestPackage(5);
        ChannelHandlerContext givenContext = givenChannel.pipeline().lastContext();
        PackageHandler<?> givenHandler = new PackageHandler<>(TestPackage.class) {

            @Override
            protected CompletableFuture<HandlingResult> handleAsyncInternal(TestPackage pkg, ChannelHandlerContext context) {
                return completedFuture(new ReplyAndClose(new TestPackage(pkg.value() + 1)));
            }
        };

        givenHandler.handleAsync(givenPackage, givenContext);
        givenChannel.runPendingTasks();

        RuntimeException actual = assertThrows(RuntimeException.class, givenChannel::checkException);
        assertSame(givenException, actual);
        assertNull(givenChannel.readOutbound());
        assertFalse(givenChannel.isOpen());
    }

    @Test
    public void responseCallbackShouldBeHandledByEventLoopThread() {
        TestPackage givenPackage = new TestPackage(5);
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        EventLoop givenEventLoop = mock(EventLoop.class);
        when(givenChannel.eventLoop()).thenReturn(givenEventLoop);

        handler.handleAsync(givenPackage, givenContext);

        verify(givenEventLoop).execute(any());
    }

    private record TestPackage(int value) {
    }

    private static final class TestPackageHandler extends PackageHandler<TestPackage> {

        public TestPackageHandler() {
            super(TestPackage.class);
        }

        @Override
        protected CompletableFuture<HandlingResult> handleAsyncInternal(TestPackage pkg, ChannelHandlerContext context) {
            return completedFuture(new Reply(new TestPackage(pkg.value() + 1)));
        }
    }
}
