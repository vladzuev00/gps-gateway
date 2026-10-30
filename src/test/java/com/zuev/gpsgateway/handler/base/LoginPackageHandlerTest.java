package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.Reply;
import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.model.TrackerLoginResult.Success;
import com.zuev.gpsgateway.service.MessageService;
import com.zuev.gpsgateway.service.TrackerChannelRegistry;
import com.zuev.gpsgateway.service.TrackerLoginService;
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

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.*;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class LoginPackageHandlerTest {

    @Mock
    private TrackerLoginService mockedTrackerLoginService;

    @Mock
    private MessageService mockedMessageService;

    @Mock
    private TrackerChannelRegistry mockedTrackerChannelRegistry;

    private TestLoginPackageHandler handler;

    @BeforeEach
    public void initializeHandler() {
        handler = new TestLoginPackageHandler(
                mockedTrackerLoginService,
                mockedMessageService,
                mockedTrackerChannelRegistry
        );
    }

    @Test
    public void loginPackageShouldBeHandledSuccessfully() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class)) {
            EmbeddedChannel givenChannel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
            ChannelHandlerContext givenContext = givenChannel.pipeline().firstContext();
            TestLoginPackage givenPackage = new TestLoginPackage("356307042441013", "pass");

            Tracker givenTracker = new Tracker(255, "356307042441013");
            when(mockedTrackerLoginService.loginAsync("356307042441013", "pass"))
                    .thenReturn(completedFuture(new Success(givenTracker)));

            Message givenLastMessage = new Message(
                    37.6173,
                    55.7558,
                    1730289600000L,
                    60.,
                    90.,
                    150.,
                    10,
                    1.2,
                    Map.of("fuel", "45"),
                    255
            );
            when(mockedMessageService.findLastMessageAsync(255))
                    .thenReturn(completedFuture(Optional.of(givenLastMessage)));

            CompletableFuture<HandlingResult> future = handler.handleAsyncInternal(givenPackage, givenContext);
            givenChannel.runPendingTasks();

            HandlingResult actual = future.join();
            HandlingResult expected = new Reply(new SuccessResponse(255, "356307042441013"));
            assertEquals(expected, actual);

            mockedChannelAttributeUtil.verify(() -> setImei(givenContext, "356307042441013"));
            mockedChannelAttributeUtil.verify(() -> setLastMessage(givenContext, givenLastMessage));
            mockedChannelAttributeUtil.verify(() -> setTracker(givenContext, givenTracker));
            verify(mockedTrackerChannelRegistry).register(255, givenChannel);
        }
    }

    private record TestLoginPackage(String imei, String password) {
    }

    private record SuccessResponse(long trackerId, String imei) {
    }

    private record UnknownImeiResponse(String imei) {
    }

    private record WrongPasswordResponse(String imei) {
    }

    private static final class TestLoginPackageHandler extends LoginPackageHandler<TestLoginPackage> {

        public TestLoginPackageHandler(TrackerLoginService trackerLoginService,
                                       MessageService messageService,
                                       TrackerChannelRegistry trackerChannelRegistry) {
            super(TestLoginPackage.class, trackerLoginService, messageService, trackerChannelRegistry);
        }

        @Override
        protected String getImei(TestLoginPackage pkg) {
            return pkg.imei();
        }

        @Override
        protected String getPassword(TestLoginPackage pkg) {
            return pkg.password();
        }

        @Override
        protected SuccessResponse createSuccessResponse(TestLoginPackage pkg, Tracker tracker) {
            return new SuccessResponse(tracker.id(), pkg.imei());
        }

        @Override
        protected UnknownImeiResponse createUnknownImeiResponse(TestLoginPackage pkg) {
            return new UnknownImeiResponse(pkg.imei());
        }

        @Override
        protected WrongPasswordResponse createWrongPasswordResponse(TestLoginPackage pkg) {
            return new WrongPasswordResponse(pkg.imei());
        }
    }
}
