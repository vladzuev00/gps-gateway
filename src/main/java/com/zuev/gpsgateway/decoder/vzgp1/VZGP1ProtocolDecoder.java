package com.zuev.gpsgateway.decoder.vzgp1;

import com.zuev.gpsgateway.decoder.base.BinaryProtocolDecoder;
import com.zuev.gpsgateway.decoder.vzgp1.pkg.VZGP1AuthPackageDecoder;
import com.zuev.gpsgateway.decoder.vzgp1.pkg.VZGP1BlackBoxPackageDecoder;
import com.zuev.gpsgateway.decoder.vzgp1.pkg.VZGP1DataPackageDecoder;
import com.zuev.gpsgateway.decoder.vzgp1.pkg.VZGP1PingPackageDecoder;
import com.zuev.gpsgateway.util.CRC16Util;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.OptionalInt;

public final class VZGP1ProtocolDecoder extends BinaryProtocolDecoder {
    private static final int PACKAGE_PREFIX_LENGTH = 2;
    private static final int PACKAGE_TYPE_LENGTH = 1;
    private static final int PAYLOAD_LENGTH_LENGTH = 2;
    private static final int CHECKSUM_LENGTH = 2;
    private static final int EMPTY_PACKAGE_LENGTH = PACKAGE_PREFIX_LENGTH + PACKAGE_TYPE_LENGTH + PAYLOAD_LENGTH_LENGTH
            + CHECKSUM_LENGTH;

    public VZGP1ProtocolDecoder(VZGP1AuthPackageDecoder authPackageDecoder,
                                VZGP1PingPackageDecoder pingPackageDecoder,
                                VZGP1DataPackageDecoder dataPackageDecoder,
                                VZGP1BlackBoxPackageDecoder blackBoxPackageDecoder) {
        super(List.of(authPackageDecoder, pingPackageDecoder, dataPackageDecoder, blackBoxPackageDecoder));
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
        return CRC16Util.calculate(
                byteBuf,
                byteBuf.readerIndex() + PACKAGE_PREFIX_LENGTH,
                byteBuf.writerIndex() - CHECKSUM_LENGTH
        );
    }
}
