package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryDataPackage;
import com.zuev.gpsgateway.model.cherry.CherryMessage;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public final class CherryDataPackageDecoder extends CherryPackageDecoder {
    private static final String PREFIX = "@DATA@";

    private final CherryMessageDecoder messageDecoder;

    public CherryDataPackageDecoder(CherryMessageDecoder messageDecoder) {
        super(PREFIX);
        this.messageDecoder = messageDecoder;
    }

    @Override
    protected CherryDataPackage decodeFields(Iterator<String> iterator) {
        CherryMessage message = messageDecoder.decode(iterator);
        return new CherryDataPackage(message);
    }
}
