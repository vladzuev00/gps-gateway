package com.zuev.gpsgateway.service;

import com.zuev.gpsgateway.model.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

//TODO implement publishing to Kafka(topic tracker.messages.raw, key = trackerId)
@Service
public final class MessageService {

    public CompletableFuture<Void> publishAllAsync(@SuppressWarnings("unused") List<Message> messages) {
        return completedFuture(null);
    }
}
