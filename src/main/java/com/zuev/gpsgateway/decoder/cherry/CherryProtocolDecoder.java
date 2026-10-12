package com.zuev.gpsgateway.decoder.cherry;

import com.zuev.gpsgateway.decoder.base.TextProtocolDecoder;
import com.zuev.gpsgateway.decoder.cherry.pkg.CherryPackageDecoder;
import com.zuev.gpsgateway.exception.InvalidChecksumException;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.OptionalInt;

import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.US_ASCII;

public final class CherryProtocolDecoder extends TextProtocolDecoder {
    private static final byte PACKAGE_END = '\0';
    private static final int NO_SUCH_OCCURRENCE = -1;
    private static final byte FIELD_SEPARATOR = ';';
    private static final int PACKAGE_END_LENGTH = 1;

    public CherryProtocolDecoder(List<CherryPackageDecoder> packageDecoders) {
        super(packageDecoders, US_ASCII);
    }

    @Override
    protected OptionalInt findCompletePackageEnd(ByteBuf byteBuf) {
        int packageEndIndex = byteBuf.indexOf(byteBuf.readerIndex(), byteBuf.writerIndex(), PACKAGE_END);
        return packageEndIndex != NO_SUCH_OCCURRENCE ? OptionalInt.of(packageEndIndex) : OptionalInt.empty();
    }

    @Override
    protected OptionalInt getChecksum(ByteBuf byteBuf) {
        try {
            int lastFieldSeparatorIndex = byteBuf.indexOf(byteBuf.writerIndex(), byteBuf.readerIndex(), FIELD_SEPARATOR);
            if (lastFieldSeparatorIndex == NO_SUCH_OCCURRENCE) {
                return OptionalInt.empty();
            }
            int checksumStartIndex = lastFieldSeparatorIndex + 1;
            int checksumLength = byteBuf.writerIndex() - PACKAGE_END_LENGTH - checksumStartIndex;
            String checksum = byteBuf.toString(checksumStartIndex, checksumLength, US_ASCII);
            return OptionalInt.of(parseInt(checksum));
        } catch (NumberFormatException cause) {
            throw new InvalidChecksumException(cause);
        }
    }

    //TODO refactor
    @Override
    protected int calculateChecksum(ByteBuf byteBuf) {
        int lastSeparatorIndex = byteBuf.indexOf(byteBuf.writerIndex(), byteBuf.readerIndex(), FIELD_SEPARATOR);
        int sum = 0;
        for (int i = byteBuf.readerIndex(); i <= lastSeparatorIndex; i++) {
            sum += byteBuf.getUnsignedByte(i);
        }
        return sum;
    }
}
