package com.zuev.gpsgateway.service;

import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.model.TrackerLoginResult;
import com.zuev.gpsgateway.model.TrackerLoginResult.Success;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

//TODO implement and test
@Service
public final class TrackerLoginService {

    public CompletableFuture<TrackerLoginResult> loginAsync(String imei, @SuppressWarnings("unused") String password) {
        return completedFuture(new Success(new Tracker(imei.hashCode(), imei)));
    }
}
