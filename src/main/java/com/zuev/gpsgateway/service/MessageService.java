package com.zuev.gpsgateway.service;

import com.zuev.gpsgateway.model.Message;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

//TODO refactor and test
//TODO replace stub with request to tracker login service
@Service
public class MessageService {

    public CompletableFuture<Optional<Message>> findLastMessageAsync(long trackerId) {
        return CompletableFuture.completedFuture(Optional.empty());
    }
}
