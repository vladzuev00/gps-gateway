package com.zuev.gpsgateway.service;

import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.model.TrackerLoginResult;
import org.springframework.stereotype.Service;

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
    public CompletableFuture<TrackerLoginResult> login(String imei, String password) {
        return completedFuture(new TrackerLoginResult.Success(new Tracker(STUB_TRACKER_ID, imei)));
    }
}
