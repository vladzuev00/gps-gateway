package com.zuev.gpsgateway.util;

import com.zuev.gpsgateway.model.Tracker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ChannelAttributeUtilTest {
    private final EmbeddedChannel channel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
    private final ChannelHandlerContext context = channel.pipeline().firstContext();

    @Test
    public void imeiShouldBeSet() {
        String givenImei = "356307042441013";

        setImei(context, givenImei);

        String actual = channel.attr(IMEI_KEY).get();
        assertSame(givenImei, actual);
    }

    @Test
    public void imeiShouldBeFound() {
        String givenImei = "356307042441013";
        channel.attr(IMEI_KEY).set(givenImei);

        Optional<String> optionalActual = findImei(context);
        assertTrue(optionalActual.isPresent());
        String actual = optionalActual.get();
        assertSame(givenImei, actual);
    }

    @Test
    public void imeiShouldNotBeFound() {
        Optional<String> actual = findImei(context);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void trackerShouldBeSet() {
        Tracker givenTracker = new Tracker(255, "356307042441013");

        setTracker(context, givenTracker);

        Tracker actual = channel.attr(TRACKER_KEY).get();
        assertSame(givenTracker, actual);
    }

    @Test
    public void trackerShouldBeFound() {
        Tracker givenTracker = new Tracker(255, "356307042441013");
        channel.attr(TRACKER_KEY).set(givenTracker);

        Optional<Tracker> optionalActual = findTracker(context);
        assertTrue(optionalActual.isPresent());
        Tracker actual = optionalActual.get();
        assertSame(givenTracker, actual);
    }

    @Test
    public void trackerShouldNotBeFound() {
        Optional<Tracker> actual = findTracker(context);
        assertTrue(actual.isEmpty());
    }
}
