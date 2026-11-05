package com.zuev.gpsgateway.registry;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public final class TrackerChannelRegistry {
    private final Map<Long, Channel> channelsByTrackerIds = new ConcurrentHashMap<>();

    public void register(long trackerId, Channel channel) {
        channelsByTrackerIds.put(trackerId, channel);
        channel.closeFuture().addListener(future -> channelsByTrackerIds.remove(trackerId, channel));
    }

    public Optional<Channel> findChannel(long trackerId) {
        return Optional.ofNullable(channelsByTrackerIds.get(trackerId));
    }
}
