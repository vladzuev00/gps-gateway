package com.zuev.gpsgateway.exceptionhandler.lime;

import com.zuev.gpsgateway.exception.NoSuitableDecoderException;
import com.zuev.gpsgateway.exception.NotLoggedInTrackerException;
import com.zuev.gpsgateway.exception.lime.LimeMissingTagException;
import com.zuev.gpsgateway.exception.lime.LimeUnknownTagException;
import com.zuev.gpsgateway.model.lime.LimeErrorResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class LimeExceptionHandlerTest {
    private final LimeExceptionHandler handler = new LimeExceptionHandler();

    @Test
    public void unknownTagExceptionShouldBeHandled() {
        EmbeddedChannel givenChannel = new EmbeddedChannel(handler);
        ChannelHandlerContext givenContext = givenChannel.pipeline().context(handler);
        Throwable givenException = new LimeUnknownTagException("test message");

        handler.handle(givenContext, givenException);

        LimeErrorResponse actual = givenChannel.readOutbound();
        LimeErrorResponse expected = new LimeErrorResponse((byte) 0x01);
        assertEquals(expected, actual);
        assertNull(givenChannel.readOutbound());
        assertTrue(givenChannel.isOpen());
    }

    @Test
    public void noSuitableDecoderExceptionShouldBeHandled() {
        EmbeddedChannel givenChannel = new EmbeddedChannel(handler);
        ChannelHandlerContext givenContext = givenChannel.pipeline().context(handler);
        Throwable givenException = new NoSuitableDecoderException("test message");

        handler.handle(givenContext, givenException);

        LimeErrorResponse actual = givenChannel.readOutbound();
        LimeErrorResponse expected = new LimeErrorResponse((byte) 0x01);
        assertEquals(expected, actual);
        assertNull(givenChannel.readOutbound());
        assertTrue(givenChannel.isOpen());
    }

    @Test
    public void missingTagExceptionShouldBeHandled() {
        EmbeddedChannel givenChannel = new EmbeddedChannel(handler);
        ChannelHandlerContext givenContext = givenChannel.pipeline().context(handler);
        Throwable givenException = new LimeMissingTagException("test message");

        handler.handle(givenContext, givenException);

        LimeErrorResponse actual = givenChannel.readOutbound();
        LimeErrorResponse expected = new LimeErrorResponse((byte) 0x02);
        assertEquals(expected, actual);
        assertNull(givenChannel.readOutbound());
        assertTrue(givenChannel.isOpen());
    }

    @Test
    public void notLoggedInTrackerExceptionShouldBeHandled() {
        EmbeddedChannel givenChannel = new EmbeddedChannel(handler);
        ChannelHandlerContext givenContext = givenChannel.pipeline().context(handler);
        Throwable givenException = new NotLoggedInTrackerException();

        handler.handle(givenContext, givenException);

        LimeErrorResponse actual = givenChannel.readOutbound();
        LimeErrorResponse expected = new LimeErrorResponse((byte) 0x03);
        assertEquals(expected, actual);
        assertNull(givenChannel.readOutbound());
        assertFalse(givenChannel.isOpen());
    }

    @Test
    public void unknownExceptionShouldBeHandled() {
        EmbeddedChannel givenChannel = new EmbeddedChannel(handler);
        ChannelHandlerContext givenContext = givenChannel.pipeline().context(handler);
        Throwable givenException = new RuntimeException();

        handler.handle(givenContext, givenException);

        assertNull(givenChannel.readOutbound());
        assertFalse(givenChannel.isOpen());
    }
}
