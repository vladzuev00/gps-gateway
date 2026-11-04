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
    private static final AttributeKey<String> IMEI_KEY = AttributeKey.valueOf("imei");
    private static final AttributeKey<Tracker> TRACKER_KEY = AttributeKey.valueOf("tracker");

    public static void setImei(ChannelHandlerContext context, String imei) {
        context.channel().attr(IMEI_KEY).set(imei);
    }

    public static Optional<String> findImei(ChannelHandlerContext context) {
        return ofNullable(context.channel().attr(IMEI_KEY).get());
    }

    public static void setTracker(ChannelHandlerContext context, Tracker tracker) {
        context.channel().attr(TRACKER_KEY).set(tracker);
    }

    public static Optional<Tracker> findTracker(ChannelHandlerContext context) {
        return ofNullable(context.channel().attr(TRACKER_KEY).get());
    }
}
