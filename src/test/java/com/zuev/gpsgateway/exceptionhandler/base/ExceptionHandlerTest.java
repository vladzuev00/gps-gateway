package com.zuev.gpsgateway.exceptionhandler.base;

import com.zuev.gpsgateway.util.ChannelAttributeUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.Optional;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.findImei;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class ExceptionHandlerTest {
    private final TestExceptionHandler handler = new TestExceptionHandler();

    @Test
    public void exceptionShouldBeCaught() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class);
             LogCaptor givenLogCaptor = LogCaptor.forClass(ExceptionHandler.class)) {
            ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
            RuntimeException givenException = new RuntimeException("test exception");

            mockedChannelAttributeUtil.when(() -> findImei(same(givenContext)))
                    .thenReturn(Optional.of("00000355234055650192"));

            Channel givenChannel = mock(Channel.class);
            when(givenContext.channel()).thenReturn(givenChannel);
            when(givenChannel.toString()).thenReturn("test channel");

            handler.exceptionCaught(givenContext, givenException);

            List<String> actualLogs = givenLogCaptor.getErrorLogs();
            List<String> expectedLogs = List.of(
                    "Exception caught on channel test channel, imei: 00000355234055650192"
            );
            assertEquals(expectedLogs, actualLogs);
            assertSame(givenContext, handler.context);
            assertSame(givenException, handler.exception);
        }
    }

    @Test
    public void exceptionShouldBeCaughtWithUnknownImei() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class);
             LogCaptor givenLogCaptor = LogCaptor.forClass(ExceptionHandler.class)) {
            ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
            RuntimeException givenException = new RuntimeException("test exception");

            mockedChannelAttributeUtil.when(() -> findImei(same(givenContext))).thenReturn(Optional.empty());

            Channel givenChannel = mock(Channel.class);
            when(givenContext.channel()).thenReturn(givenChannel);
            when(givenChannel.toString()).thenReturn("test channel");

            handler.exceptionCaught(givenContext, givenException);

            List<String> actualLogs = givenLogCaptor.getErrorLogs();
            List<String> expectedLogs = List.of("Exception caught on channel test channel");
            assertEquals(expectedLogs, actualLogs);
            assertSame(givenContext, handler.context);
            assertSame(givenException, handler.exception);
        }
    }

    @Test
    public void decoderExceptionShouldBeUnwrapped() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class)) {
            ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
            RuntimeException givenCause = new RuntimeException("test cause");
            DecoderException givenException = new DecoderException(givenCause);

            mockedChannelAttributeUtil.when(() -> findImei(same(givenContext))).thenReturn(Optional.empty());

            handler.exceptionCaught(givenContext, givenException);

            assertSame(givenCause, handler.exception);
        }
    }

    private static final class TestExceptionHandler extends ExceptionHandler {
        private ChannelHandlerContext context;
        private Throwable exception;

        @Override
        protected void handle(ChannelHandlerContext context, Throwable exception) {
            this.context = context;
            this.exception = exception;
        }
    }
}
