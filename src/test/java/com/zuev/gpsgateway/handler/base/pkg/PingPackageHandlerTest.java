package com.zuev.gpsgateway.handler.base.pkg;

import com.zuev.gpsgateway.handler.base.pkg.PackageHandler.HandlingResult;
import com.zuev.gpsgateway.handler.base.pkg.PackageHandler.HandlingResult.Reply;
import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class PingPackageHandlerTest {
    private final TestPingPackageHandler handler = new TestPingPackageHandler();

    @Test
    public void pingPackageShouldBeHandled() {
        TestPingPackage givenPackage = new TestPingPackage("356307042441013");
        ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        HandlingResult actual = handler.handleAsyncInternal(givenPackage, givenContext).join();
        HandlingResult expected = new Reply(new TestPingResponse("356307042441013"));
        assertEquals(expected, actual);

        verifyNoInteractions(givenContext);
    }

    private record TestPingPackage(String imei) {
    }

    private record TestPingResponse(String imei) {
    }

    private static final class TestPingPackageHandler extends PingPackageHandler<TestPingPackage> {

        public TestPingPackageHandler() {
            super(TestPingPackage.class);
        }

        @Override
        protected TestPingResponse createResponse(TestPingPackage pkg) {
            return new TestPingResponse(pkg.imei());
        }
    }
}
