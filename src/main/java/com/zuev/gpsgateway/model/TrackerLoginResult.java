package com.zuev.gpsgateway.model;

//TODO refactor
public sealed interface TrackerLoginResult {

    record Success(Tracker tracker) implements TrackerLoginResult {
    }

    record UnknownImei() implements TrackerLoginResult {
    }

    record WrongPassword() implements TrackerLoginResult {
    }
}
