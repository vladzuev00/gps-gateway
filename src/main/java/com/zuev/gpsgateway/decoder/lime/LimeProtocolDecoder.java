package com.zuev.gpsgateway.decoder.lime;

import com.zuev.gpsgateway.decoder.base.BinaryProtocolDecoder;
import com.zuev.gpsgateway.decoder.lime.pkg.LimePackageDecoder;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.OptionalInt;

public final class LimeProtocolDecoder extends BinaryProtocolDecoder {
    private static final int TAG_VALUE_PAIRS_LENGTH_LENGTH = 2;

    public LimeProtocolDecoder(List<LimePackageDecoder> packageDecoders) {
        super(packageDecoders);
    }

    @Override
    protected OptionalInt findCompletePackageEnd(ByteBuf byteBuf) {
        if (byteBuf.readableBytes() < TAG_VALUE_PAIRS_LENGTH_LENGTH) {
            return OptionalInt.empty();
        }
        int tagValuePairsLength = byteBuf.getUnsignedShortLE(byteBuf.readerIndex());
        int completePackageLength = TAG_VALUE_PAIRS_LENGTH_LENGTH + tagValuePairsLength;
        if (byteBuf.readableBytes() < completePackageLength) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(byteBuf.readerIndex() + completePackageLength - 1);
    }

    @Override
    protected OptionalInt getChecksum(ByteBuf byteBuf) {
        return OptionalInt.empty();
    }

    @Override
    protected int calculateChecksum(ByteBuf byteBuf) {
        return Integer.MIN_VALUE;
    }
}
