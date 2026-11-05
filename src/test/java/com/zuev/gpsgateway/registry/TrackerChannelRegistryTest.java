package com.zuev.gpsgateway.registry;

import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.springframework.test.util.ReflectionTestUtils.getField;

public final class TrackerChannelRegistryTest {
    private final TrackerChannelRegistry registry = new TrackerChannelRegistry();

    @Test
    public void channelShouldBeRegistered() {
        long givenTrackerId = 255;
        EmbeddedChannel givenChannel = new EmbeddedChannel();

        registry.register(givenTrackerId, givenChannel);

        Map<Long, Channel> actual = getChannelsByTrackerIds();
        Map<Long, Channel> expected = Map.of(givenTrackerId, givenChannel);
        assertEquals(expected, actual);

        givenChannel.close();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void channelShouldBeFound() {
        long givenTrackerId = 255;

        Channel givenChannel = mock(Channel.class);
        getChannelsByTrackerIds().put(givenTrackerId, givenChannel);

        Optional<Channel> actual = registry.findChannel(givenTrackerId);
        Optional<Channel> expected = Optional.of(givenChannel);
        assertEquals(expected, actual);
    }

    @Test
    public void channelShouldNotBeFound() {
        long givenTrackerId = 255;

        Channel givenChannel = mock(Channel.class);
        getChannelsByTrackerIds().put(256L, givenChannel);

        Optional<Channel> actual = registry.findChannel(givenTrackerId);
        assertTrue(actual.isEmpty());
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Channel> getChannelsByTrackerIds() {
        return (Map<Long, Channel>) getField(registry, "channelsByTrackerIds");
    }
}
