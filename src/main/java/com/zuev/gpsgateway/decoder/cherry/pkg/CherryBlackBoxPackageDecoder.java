package com.zuev.gpsgateway.decoder.cherry.pkg;

import com.zuev.gpsgateway.model.cherry.CherryBlackBoxPackage;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Iterator;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Component
public final class CherryBlackBoxPackageDecoder extends CherryPackageDecoder {
    private static final String PREFIX = "@BLACKBOX@";

    private final CherryMessageDecoder messageDecoder;

    public CherryBlackBoxPackageDecoder(CherryMessageDecoder messageDecoder) {
        super(PREFIX);
        this.messageDecoder = messageDecoder;
    }

    @Override
    protected CherryBlackBoxPackage decodeFields(String[] fields) {
        Iterator<String> fieldIterator = Arrays.asList(fields).iterator();
        int messageCount = Integer.parseInt(fieldIterator.next());
        return range(0, messageCount)
                .mapToObj(i -> messageDecoder.decode(fieldIterator))
                .collect(collectingAndThen(toList(), CherryBlackBoxPackage::new));
    }
}
