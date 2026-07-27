package com.zuev.gpsgateway.decoder.mango;

import com.zuev.gpsgateway.decoder.base.BinaryProtocolDecoder;
import com.zuev.gpsgateway.decoder.mango.pkg.MangoLoginPackageDecoder;
import com.zuev.gpsgateway.decoder.mango.pkg.MangoBlackBoxPackageDecoder;
import com.zuev.gpsgateway.decoder.mango.pkg.MangoDataPackageDecoder;
import com.zuev.gpsgateway.decoder.mango.pkg.MangoPingPackageDecoder;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.OptionalInt;

public final class MangoProtocolDecoder extends BinaryProtocolDecoder {
    private static final int PACKAGE_PREFIX_LENGTH = 2;
    private static final int PACKAGE_TYPE_LENGTH = 1;
    private static final int PAYLOAD_LENGTH_LENGTH = 2;
    private static final int CHECKSUM_LENGTH = 2;
    private static final int EMPTY_PACKAGE_LENGTH = PACKAGE_PREFIX_LENGTH + PACKAGE_TYPE_LENGTH + PAYLOAD_LENGTH_LENGTH
            + CHECKSUM_LENGTH;

    public MangoProtocolDecoder(MangoLoginPackageDecoder loginPackageDecoder,
                                MangoPingPackageDecoder pingPackageDecoder,
                                MangoDataPackageDecoder dataPackageDecoder,
                                MangoBlackBoxPackageDecoder blackBoxPackageDecoder) {
        super(List.of(loginPackageDecoder, pingPackageDecoder, dataPackageDecoder, blackBoxPackageDecoder));
    }

    @Override
    protected OptionalInt findCompletePackageEnd(ByteBuf byteBuf) {
        if (byteBuf.readableBytes() < EMPTY_PACKAGE_LENGTH) {
            return OptionalInt.empty();
        }
        int payloadLength = byteBuf.getUnsignedShort(byteBuf.readerIndex() + PACKAGE_PREFIX_LENGTH + PACKAGE_TYPE_LENGTH);
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
        for (int i = byteBuf.readerIndex() + PACKAGE_PREFIX_LENGTH; i < byteBuf.writerIndex() - CHECKSUM_LENGTH; i++) {
            sum += byteBuf.getUnsignedByte(i);
        }
        return sum & 0xFFFF;
    }
}
