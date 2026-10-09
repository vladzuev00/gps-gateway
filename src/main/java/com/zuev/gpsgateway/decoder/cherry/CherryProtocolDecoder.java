package com.zuev.gpsgateway.decoder.cherry;

import com.zuev.gpsgateway.decoder.base.TextProtocolDecoder;
import com.zuev.gpsgateway.decoder.cherry.pkg.CherryPackageDecoder;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.OptionalInt;

import static java.nio.charset.StandardCharsets.US_ASCII;

//TODO refactor
public final class CherryProtocolDecoder extends TextProtocolDecoder {
    private static final byte PACKAGE_END = '\0';
    private static final byte FIELD_SEPARATOR = ';';
    private static final int PACKAGE_END_LENGTH = 1;

    public CherryProtocolDecoder(List<CherryPackageDecoder> packageDecoders) {
        super(packageDecoders, US_ASCII);
    }

    @Override
    protected OptionalInt findCompletePackageEnd(ByteBuf byteBuf) {
        int index = byteBuf.indexOf(byteBuf.readerIndex(), byteBuf.writerIndex(), PACKAGE_END);
        return index != -1 ? OptionalInt.of(index) : OptionalInt.empty();
    }

    @Override
    protected OptionalInt getChecksum(ByteBuf byteBuf) {
        int lastSeparatorIndex = findLastSeparatorIndex(byteBuf);
        if (lastSeparatorIndex == -1) {
            return OptionalInt.empty();
        }
        int checksumStart = lastSeparatorIndex + 1;
        int checksumLength = byteBuf.writerIndex() - PACKAGE_END_LENGTH - checksumStart;
        String checksum = byteBuf.toString(checksumStart, checksumLength, US_ASCII);
        return OptionalInt.of(Integer.parseInt(checksum));
    }

    @Override
    protected int calculateChecksum(ByteBuf byteBuf) {
        int lastSeparatorIndex = findLastSeparatorIndex(byteBuf);
        int sum = 0;
        for (int i = byteBuf.readerIndex(); i <= lastSeparatorIndex; i++) {
            sum += byteBuf.getUnsignedByte(i);
        }
        return sum;
    }

    private static int findLastSeparatorIndex(ByteBuf byteBuf) {
        return byteBuf.indexOf(byteBuf.writerIndex(), byteBuf.readerIndex(), FIELD_SEPARATOR);
    }
}
