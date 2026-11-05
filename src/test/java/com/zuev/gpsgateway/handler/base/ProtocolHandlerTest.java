package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.exception.NoSuitableHandlerException;
import com.zuev.gpsgateway.handler.base.pkg.PackageHandler;
import com.zuev.gpsgateway.util.ChannelAttributeUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.findImei;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ProtocolHandlerTest {

    @Mock
    private PackageHandler<Object> mockedFirstPackageHandler;

    @Mock
    private PackageHandler<Object> mockedSecondPackageHandler;

    @Mock
    private PackageHandler<Object> mockedThirdPackageHandler;

    private ProtocolHandler handler;

    @BeforeEach
    public void initializeHandler() {
        handler = new ProtocolHandler(
                List.of(
                        mockedFirstPackageHandler,
                        mockedSecondPackageHandler,
                        mockedThirdPackageHandler
                )
        );
    }

    @Test
    public void channelShouldBeActivated()
            throws Exception {
        try (LogCaptor givenLogCaptor = LogCaptor.forClass(ProtocolHandler.class)) {
            ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

            Channel givenChannel = mock(Channel.class);
            when(givenContext.channel()).thenReturn(givenChannel);
            when(givenChannel.toString()).thenReturn("test channel");

            handler.channelActive(givenContext);

            List<String> actualLogs = givenLogCaptor.getInfoLogs();
            List<String> expectedLogs = List.of("Channel is active: test channel");
            assertEquals(expectedLogs, actualLogs);

            verify(givenContext).fireChannelActive();
            verifyNoInteractions(mockedFirstPackageHandler, mockedSecondPackageHandler, mockedThirdPackageHandler);
        }
    }

    @Test
    public void channelShouldBeInactivated() throws Exception {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class);
             LogCaptor givenLogCaptor = LogCaptor.forClass(ProtocolHandler.class)) {
            ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

            mockedChannelAttributeUtil.when(() -> findImei(same(givenContext)))
                    .thenReturn(Optional.of("00000355234055650192"));

            Channel givenChannel = mock(Channel.class);
            when(givenContext.channel()).thenReturn(givenChannel);
            when(givenChannel.toString()).thenReturn("test channel");

            handler.channelInactive(givenContext);

            List<String> actualLogs = givenLogCaptor.getInfoLogs();
            List<String> expectedLogs = List.of("Channel is inactive: test channel, imei: 00000355234055650192");
            assertEquals(expectedLogs, actualLogs);

            verify(givenContext).fireChannelInactive();
            verifyNoInteractions(mockedFirstPackageHandler, mockedSecondPackageHandler, mockedThirdPackageHandler);
        }
    }

    @Test
    public void channelShouldBeInactivatedWithUnknownImei() throws Exception {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class);
             LogCaptor givenLogCaptor = LogCaptor.forClass(ProtocolHandler.class)) {
            ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

            mockedChannelAttributeUtil.when(() -> findImei(same(givenContext))).thenReturn(Optional.empty());

            Channel givenChannel = mock(Channel.class);
            when(givenContext.channel()).thenReturn(givenChannel);
            when(givenChannel.toString()).thenReturn("test channel");

            handler.channelInactive(givenContext);

            List<String> actualLogs = givenLogCaptor.getInfoLogs();
            List<String> expectedLogs = List.of("Channel is inactive: test channel");
            assertEquals(expectedLogs, actualLogs);

            verify(givenContext).fireChannelInactive();
            verifyNoInteractions(mockedFirstPackageHandler, mockedSecondPackageHandler, mockedThirdPackageHandler);
        }
    }

    @Test
    public void channelShouldBeRead() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class);
             LogCaptor givenLogCaptor = LogCaptor.forClass(ProtocolHandler.class)) {
            ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
            TestPackage givenPackage = new TestPackage(5);
            givenLogCaptor.setLogLevelToDebug();

            mockedChannelAttributeUtil.when(() -> findImei(same(givenContext)))
                    .thenReturn(Optional.of("00000355234055650192"));

            Channel givenChannel = mock(Channel.class);
            when(givenContext.channel()).thenReturn(givenChannel);
            when(givenChannel.toString()).thenReturn("test channel");

            when(mockedFirstPackageHandler.isAbleHandle(same(givenPackage))).thenReturn(false);
            when(mockedSecondPackageHandler.isAbleHandle(same(givenPackage))).thenReturn(true);

            handler.channelRead(givenContext, givenPackage);

            List<String> actualLogs = givenLogCaptor.getDebugLogs();
            List<String> expectedLogs = List.of("Handling package TestPackage[value=5] on channel test channel, imei: 00000355234055650192");
            assertEquals(expectedLogs, actualLogs);

            verify(mockedSecondPackageHandler).handleAsync(same(givenPackage), same(givenContext));
            verify(mockedFirstPackageHandler, never()).handleAsync(any(), any());
            verifyNoInteractions(mockedThirdPackageHandler);
        }
    }

    @Test
    public void channelShouldBeReadWithUnknownImei() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class);
             LogCaptor givenLogCaptor = LogCaptor.forClass(ProtocolHandler.class)) {
            ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
            TestPackage givenPackage = new TestPackage(5);
            givenLogCaptor.setLogLevelToDebug();

            mockedChannelAttributeUtil.when(() -> findImei(same(givenContext))).thenReturn(Optional.empty());

            Channel givenChannel = mock(Channel.class);
            when(givenContext.channel()).thenReturn(givenChannel);
            when(givenChannel.toString()).thenReturn("test channel");

            when(mockedFirstPackageHandler.isAbleHandle(same(givenPackage))).thenReturn(false);
            when(mockedSecondPackageHandler.isAbleHandle(same(givenPackage))).thenReturn(true);

            handler.channelRead(givenContext, givenPackage);

            List<String> actualLogs = givenLogCaptor.getDebugLogs();
            List<String> expectedLogs = List.of("Handling package TestPackage[value=5] on channel test channel");
            assertEquals(expectedLogs, actualLogs);

            verify(mockedSecondPackageHandler).handleAsync(same(givenPackage), same(givenContext));
            verify(mockedFirstPackageHandler, never()).handleAsync(any(), any());
            verifyNoInteractions(mockedThirdPackageHandler);
        }
    }

    @Test
    public void channelShouldNotBeReadBecauseOfNoSuitablePackageHandler() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class);
             LogCaptor givenLogCaptor = LogCaptor.forClass(ProtocolHandler.class)) {
            ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
            TestPackage givenPackage = new TestPackage(5);
            givenLogCaptor.setLogLevelToDebug();

            mockedChannelAttributeUtil.when(() -> findImei(same(givenContext))).thenReturn(Optional.empty());

            Channel givenChannel = mock(Channel.class);
            when(givenContext.channel()).thenReturn(givenChannel);
            when(givenChannel.toString()).thenReturn("test channel");

            when(mockedFirstPackageHandler.isAbleHandle(same(givenPackage))).thenReturn(false);
            when(mockedSecondPackageHandler.isAbleHandle(same(givenPackage))).thenReturn(false);
            when(mockedThirdPackageHandler.isAbleHandle(same(givenPackage))).thenReturn(false);

            assertThrows(NoSuitableHandlerException.class, () -> handler.channelRead(givenContext, givenPackage));

            List<String> actualLogs = givenLogCaptor.getDebugLogs();
            List<String> expectedLogs = List.of("Handling package TestPackage[value=5] on channel test channel");
            assertEquals(expectedLogs, actualLogs);

            verify(mockedFirstPackageHandler, never()).handleAsync(any(), any());
            verify(mockedSecondPackageHandler, never()).handleAsync(any(), any());
            verify(mockedThirdPackageHandler, never()).handleAsync(any(), any());
        }
    }

    private record TestPackage(int value) {
    }
}
