package com.zuev.gpsgateway.decoder.mango;

import com.zuev.gpsgateway.decoder.base.BinaryProtocolDecoder;
import com.zuev.gpsgateway.decoder.mango.pkg.MangoPackageDecoder;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.OptionalInt;

public final class MangoProtocolDecoder extends BinaryProtocolDecoder {
    private static final int PREFIX_LENGTH = 2;
    private static final int TYPE_LENGTH = 1;
    private static final int PAYLOAD_LENGTH_LENGTH = 2;
    private static final int CHECKSUM_LENGTH = 2;
    private static final int EMPTY_PACKAGE_LENGTH = PREFIX_LENGTH + TYPE_LENGTH + PAYLOAD_LENGTH_LENGTH
            + CHECKSUM_LENGTH;

    public MangoProtocolDecoder(List<MangoPackageDecoder> packageDecoders) {
        super(packageDecoders);
    }

    @Override
    protected OptionalInt findCompletePackageEnd(ByteBuf byteBuf) {
        if (byteBuf.readableBytes() < EMPTY_PACKAGE_LENGTH) {
            return OptionalInt.empty();
        }
        int payloadLength = byteBuf.getUnsignedShort(byteBuf.readerIndex() + PREFIX_LENGTH + TYPE_LENGTH);
        int completePackageLength = EMPTY_PACKAGE_LENGTH + payloadLength;
        if (byteBuf.readableBytes() < completePackageLength) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(byteBuf.readerIndex() + completePackageLength - 1);
    }

    @Override
    protected OptionalInt getChecksum(ByteBuf byteBuf) {
        return OptionalInt.of(byteBuf.getUnsignedShort(byteBuf.writerIndex() - Short.BYTES));
    }

    @Override
    protected int calculateChecksum(ByteBuf byteBuf) {
        int sum = 0;
        for (int i = byteBuf.readerIndex() + PREFIX_LENGTH; i < byteBuf.writerIndex() - CHECKSUM_LENGTH; i++) {
            sum += byteBuf.getUnsignedByte(i);
        }
        return sum & 0xFFFF;
    }
}
