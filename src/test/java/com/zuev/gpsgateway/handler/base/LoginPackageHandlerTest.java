package com.zuev.gpsgateway.handler.base;

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
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.*;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.junit.jupiter.api.Assertions.*;
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
