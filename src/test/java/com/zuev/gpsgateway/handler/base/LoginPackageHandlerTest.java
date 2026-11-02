package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.DoNothing;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.Reply;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.ReplyAndClose;
import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.model.TrackerLoginResult;
import com.zuev.gpsgateway.model.TrackerLoginResult.Success;
import com.zuev.gpsgateway.model.TrackerLoginResult.UnknownImei;
import com.zuev.gpsgateway.model.TrackerLoginResult.WrongPassword;
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
    private TrackerChannelRegistry mockedTrackerChannelRegistry;

    private TestLoginPackageHandler handler;

    @BeforeEach
    public void initializeHandler() {
        handler = new TestLoginPackageHandler(mockedTrackerLoginService, mockedTrackerChannelRegistry);
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

            CompletableFuture<HandlingResult> future = handler.handleAsyncInternal(givenPackage, givenContext);
            givenChannel.runPendingTasks();

            HandlingResult actual = future.join();
            HandlingResult expected = new Reply(new TestSuccessResponse(255, "356307042441013"));
            assertEquals(expected, actual);

            mockedChannelAttributeUtil.verify(() -> setImei(givenContext, "356307042441013"));
            mockedChannelAttributeUtil.verify(() -> setTracker(givenContext, givenTracker));
            verify(mockedTrackerChannelRegistry).register(255, givenChannel);
        }
    }

    @Test
    public void loginPackageShouldNotBeHandledBecauseOfUnknownImei() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class)) {
            EmbeddedChannel givenChannel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
            ChannelHandlerContext givenContext = givenChannel.pipeline().firstContext();
            TestLoginPackage givenPackage = new TestLoginPackage("356307042441013", "pass");

            when(mockedTrackerLoginService.loginAsync("356307042441013", "pass"))
                    .thenReturn(completedFuture(new UnknownImei()));

            CompletableFuture<HandlingResult> future = handler.handleAsyncInternal(givenPackage, givenContext);
            givenChannel.runPendingTasks();

            HandlingResult actual = future.join();
            HandlingResult expected = new ReplyAndClose(new TestUnknownImeiResponse("356307042441013"));
            assertEquals(expected, actual);

            mockedChannelAttributeUtil.verify(() -> setImei(givenContext, "356307042441013"));
            mockedChannelAttributeUtil.verify(() -> setTracker(any(), any()), never());
            verifyNoInteractions(mockedTrackerChannelRegistry);
        }
    }

    @Test
    public void loginPackageShouldNotBeHandledBecauseOfWrongPassword() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class)) {
            EmbeddedChannel givenChannel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
            ChannelHandlerContext givenContext = givenChannel.pipeline().firstContext();
            TestLoginPackage givenPackage = new TestLoginPackage("356307042441013", "pass");

            when(mockedTrackerLoginService.loginAsync("356307042441013", "pass"))
                    .thenReturn(completedFuture(new WrongPassword()));

            CompletableFuture<HandlingResult> future = handler.handleAsyncInternal(givenPackage, givenContext);
            givenChannel.runPendingTasks();

            HandlingResult actual = future.join();
            HandlingResult expected = new ReplyAndClose(new TestWrongPasswordResponse("356307042441013"));
            assertEquals(expected, actual);

            mockedChannelAttributeUtil.verify(() -> setImei(givenContext, "356307042441013"));
            mockedChannelAttributeUtil.verify(() -> setTracker(any(), any()), never());
            verifyNoInteractions(mockedTrackerChannelRegistry);
        }
    }

    @Test
    public void loginPackageShouldNotBeHandledBecauseOfChannelClosedDuringLogin() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class)) {
            EmbeddedChannel givenChannel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
            ChannelHandlerContext givenContext = givenChannel.pipeline().firstContext();
            TestLoginPackage givenPackage = new TestLoginPackage("356307042441013", "pass");

            CompletableFuture<TrackerLoginResult> givenLoginFuture = new CompletableFuture<>();
            when(mockedTrackerLoginService.loginAsync("356307042441013", "pass")).thenReturn(givenLoginFuture);

            CompletableFuture<HandlingResult> future = handler.handleAsyncInternal(givenPackage, givenContext);
            givenChannel.close();
            givenLoginFuture.complete(new Success(new Tracker(255, "356307042441013")));
            givenChannel.runPendingTasks();

            HandlingResult actual = future.join();
            HandlingResult expected = new DoNothing();
            assertEquals(expected, actual);

            mockedChannelAttributeUtil.verify(() -> setImei(givenContext, "356307042441013"));
            mockedChannelAttributeUtil.verify(() -> setTracker(any(), any()), never());
            verifyNoInteractions(mockedTrackerChannelRegistry);
        }
    }

    private record TestLoginPackage(String imei, String password) {
    }

    private record TestSuccessResponse(long trackerId, String imei) {
    }

    private record TestUnknownImeiResponse(String imei) {
    }

    private record TestWrongPasswordResponse(String imei) {
    }

    private static final class TestLoginPackageHandler extends LoginPackageHandler<TestLoginPackage> {

        public TestLoginPackageHandler(TrackerLoginService trackerLoginService,
                                       TrackerChannelRegistry trackerChannelRegistry) {
            super(TestLoginPackage.class, trackerLoginService, trackerChannelRegistry);
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
        protected TestSuccessResponse createSuccessResponse(TestLoginPackage pkg, Tracker tracker) {
            return new TestSuccessResponse(tracker.id(), pkg.imei());
        }

        @Override
        protected TestUnknownImeiResponse createUnknownImeiResponse(TestLoginPackage pkg) {
            return new TestUnknownImeiResponse(pkg.imei());
        }

        @Override
        protected TestWrongPasswordResponse createWrongPasswordResponse(TestLoginPackage pkg) {
            return new TestWrongPasswordResponse(pkg.imei());
        }
    }
}
