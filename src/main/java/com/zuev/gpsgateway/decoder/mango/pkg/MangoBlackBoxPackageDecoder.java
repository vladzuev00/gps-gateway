package com.zuev.gpsgateway.decoder.mango.pkg;

import com.zuev.gpsgateway.model.mango.MangoBlackBoxPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Component
public final class MangoBlackBoxPackageDecoder extends MangoPackageDecoder {
    private static final byte[] PREFIX = {0x56, 0x5A, 0x04};

    private final MangoMessageDecoder messageDecoder;

    public MangoBlackBoxPackageDecoder(MangoMessageDecoder messageDecoder) {
        super(PREFIX);
        this.messageDecoder = messageDecoder;
    }

    @Override
    protected MangoBlackBoxPackage decodePayload(ByteBuf payload) {
        int messageCount = payload.readUnsignedShort();
        return range(0, messageCount)
                .mapToObj(i -> messageDecoder.decode(payload))
                .collect(collectingAndThen(toList(), MangoBlackBoxPackage::new));
    }
}
