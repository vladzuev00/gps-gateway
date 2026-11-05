package com.zuev.gpsgateway.util;

import com.zuev.gpsgateway.model.Tracker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ChannelAttributeUtil {
    static final AttributeKey<String> IMEI_KEY = AttributeKey.valueOf("imei");
    static final AttributeKey<Tracker> TRACKER_KEY = AttributeKey.valueOf("tracker");

    public static void setImei(ChannelHandlerContext context, String imei) {
        set(context, IMEI_KEY, imei);
    }

    public static Optional<String> findImei(ChannelHandlerContext context) {
        return find(context, IMEI_KEY);
    }

    public static void setTracker(ChannelHandlerContext context, Tracker tracker) {
        set(context, TRACKER_KEY, tracker);
    }

    public static Optional<Tracker> findTracker(ChannelHandlerContext context) {
        return find(context, TRACKER_KEY);
    }

    private static <T> void set(ChannelHandlerContext context, AttributeKey<T> key, T value) {
        context.channel().attr(key).set(value);
    }

    private static <T> Optional<T> find(ChannelHandlerContext context, AttributeKey<T> key) {
        return ofNullable(context.channel().attr(key).get());
    }
}
