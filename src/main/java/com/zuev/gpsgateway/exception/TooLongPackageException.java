package com.zuev.gpsgateway.exception;

import io.netty.handler.codec.TooLongFrameException;

public final class TooLongPackageException extends TooLongFrameException {

    public TooLongPackageException(String message) {
        super(message);
    }
}
