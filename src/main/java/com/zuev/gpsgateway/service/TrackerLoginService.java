package com.zuev.gpsgateway.service;

import com.zuev.gpsgateway.model.Tracker;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

//TODO refactor and test
//TODO replace stub with request to tracker login service
@Service
public class TrackerLoginService {
    private static final long STUB_TRACKER_ID = 1;

    /**
     * @param password null if protocol doesn't transmit password
     */
    public CompletableFuture<Optional<Tracker>> login(String imei, String password) {
        return completedFuture(Optional.of(new Tracker(STUB_TRACKER_ID, imei)));
    }
}
