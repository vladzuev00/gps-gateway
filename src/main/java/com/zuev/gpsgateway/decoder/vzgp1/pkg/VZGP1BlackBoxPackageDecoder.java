package com.zuev.gpsgateway.decoder.vzgp1.pkg;

import com.zuev.gpsgateway.model.vzgp1.VZGP1BlackBoxPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Component
public final class VZGP1BlackBoxPackageDecoder extends VZGP1PackageDecoder {
    private static final byte[] PREFIX = {0x56, 0x5A, 0x04};

    private final VZGP1DataDecoder dataDecoder;

    public VZGP1BlackBoxPackageDecoder(VZGP1DataDecoder dataDecoder) {
        super(PREFIX);
        this.dataDecoder = dataDecoder;
    }

    @Override
    protected VZGP1BlackBoxPackage decodePayload(ByteBuf payload) {
        int dataCount = payload.readUnsignedShort();
        return range(0, dataCount)
                .mapToObj(i -> dataDecoder.read(payload))
                .collect(collectingAndThen(toList(), VZGP1BlackBoxPackage::new));
    }
}
