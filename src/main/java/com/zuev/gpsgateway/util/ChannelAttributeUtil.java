package com.zuev.gpsgateway.util;

import com.zuev.gpsgateway.model.Tracker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;

//TODO refactor and test
@NoArgsConstructor(access = PRIVATE)
public final class ChannelAttributeUtil {
    private static final AttributeKey<Tracker> TRACKER_KEY = AttributeKey.valueOf("tracker");

    public static void setTracker(ChannelHandlerContext context, Tracker tracker) {
        context.channel().attr(TRACKER_KEY).set(tracker);
    }

    public static Optional<Tracker> getTracker(ChannelHandlerContext context) {
        return ofNullable(context.channel().attr(TRACKER_KEY).get());
    }
}
