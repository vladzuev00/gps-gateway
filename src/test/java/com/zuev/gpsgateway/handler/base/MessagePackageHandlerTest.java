package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.exception.NotLoggedInTrackerException;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.Reply;
import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.service.MessageService;
import com.zuev.gpsgateway.util.ChannelAttributeUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.findTracker;
import static java.util.Collections.emptyMap;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class MessagePackageHandlerTest {

    @Mock
    private MessageService mockedMessageService;

    private TestMessagePackageHandler handler;

    @BeforeEach
    public void initializeHandler() {
        handler = new TestMessagePackageHandler(mockedMessageService);
    }

    @Test
    public void packageShouldBeHandled() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class)) {
            EmbeddedChannel givenChannel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
            ChannelHandlerContext givenContext = givenChannel.pipeline().firstContext();
            TestPackage givenPackage = new TestPackage(
                    List.of(
                            new TestMessageSource(27.5, 53.9, 1000),
                            new TestMessageSource(27.6, 54.0, 2000),
                            new TestMessageSource(27.7, 54.1, 3000)
                    )
            );

            mockedChannelAttributeUtil.when(() -> findTracker(same(givenContext)))
                    .thenReturn(Optional.of(new Tracker(255, "356307042441013")));

            List<Message> givenMessages = List.of(
                    new Message(27.5, 53.9, 1000, null, null, null, null, null, emptyMap(), 255),
                    new Message(27.6, 54.0, 2000, null, null, null, null, null, emptyMap(), 255),
                    new Message(27.7, 54.1, 3000, null, null, null, null, null, emptyMap(), 255)
            );
            when(mockedMessageService.saveAllAsync(givenMessages)).thenReturn(completedFuture(null));

            CompletableFuture<HandlingResult> future = handler.handleAsyncInternal(givenPackage, givenContext);
            givenChannel.runPendingTasks();
            HandlingResult actual = future.join();
            HandlingResult expected = new Reply(new TestResponse(3));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void packageShouldNotBeHandledBecauseOfNotLoggedInTracker() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class)) {
            EmbeddedChannel givenChannel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
            ChannelHandlerContext givenContext = givenChannel.pipeline().firstContext();
            TestPackage givenPackage = new TestPackage(
                    List.of(
                            new TestMessageSource(27.5, 53.9, 1000),
                            new TestMessageSource(27.6, 54.0, 2000),
                            new TestMessageSource(27.7, 54.1, 3000)
                    )
            );

            mockedChannelAttributeUtil.when(() -> findTracker(same(givenContext))).thenReturn(Optional.empty());

            assertThrows(NotLoggedInTrackerException.class, () -> handler.handleAsyncInternal(givenPackage, givenContext));
            verifyNoInteractions(mockedMessageService);
        }
    }

    private record TestPackage(List<TestMessageSource> messageSources) {
    }

    private record TestMessageSource(double longitude, double latitude, long epochMillis) {
    }

    private record TestResponse(int messageCount) {
    }

    private static final class TestMessagePackageHandler extends MessagePackageHandler<TestPackage, TestMessageSource> {

        public TestMessagePackageHandler(MessageService messageService) {
            super(TestPackage.class, messageService);
        }

        @Override
        protected List<TestMessageSource> getMessageSources(TestPackage pkg) {
            return pkg.messageSources();
        }

        @Override
        protected Message toMessage(TestMessageSource source, long trackerId) {
            return new Message(
                    source.longitude(),
                    source.latitude(),
                    source.epochMillis(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    emptyMap(),
                    trackerId
            );
        }

        @Override
        protected TestResponse createResponse(TestPackage pkg) {
            return new TestResponse(pkg.messageSources().size());
        }
    }
}
