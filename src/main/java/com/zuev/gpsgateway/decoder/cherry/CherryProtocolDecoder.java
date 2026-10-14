package com.zuev.gpsgateway.decoder.cherry;

import com.zuev.gpsgateway.decoder.base.TextProtocolDecoder;
import com.zuev.gpsgateway.decoder.cherry.pkg.CherryPackageDecoder;
import com.zuev.gpsgateway.exception.InvalidChecksumException;
import com.zuev.gpsgateway.exception.TooLongPackageException;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.OptionalInt;

import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.US_ASCII;

public final class CherryProtocolDecoder extends TextProtocolDecoder {
    static final byte PACKAGE_END = '\0';
    static final int NO_SUCH_OCCURRENCE = -1;
    static final int MAX_INCOMPLETE_PACKAGE_LENGTH = 64_000;
    static final byte FIELD_SEPARATOR = ';';
    static final byte PREFIX_BOUND = '@';
    static final int PACKAGE_END_LENGTH = 1;

    public CherryProtocolDecoder(List<CherryPackageDecoder> packageDecoders) {
        super(packageDecoders, US_ASCII);
    }

    @Override
    protected OptionalInt findCompletePackageEnd(ByteBuf byteBuf) {
        int packageEndIndex = byteBuf.indexOf(byteBuf.readerIndex(), byteBuf.writerIndex(), PACKAGE_END);
        if (packageEndIndex != NO_SUCH_OCCURRENCE) {
            return OptionalInt.of(packageEndIndex);
        } else if (byteBuf.readableBytes() > MAX_INCOMPLETE_PACKAGE_LENGTH) {
            throw new TooLongPackageException("Incomplete package exceeded " + MAX_INCOMPLETE_PACKAGE_LENGTH + " bytes");
        } else {
            return OptionalInt.empty();
        }
    }

    //TODO refactor
    @Override
    protected OptionalInt getChecksum(ByteBuf byteBuf) {
        int checksumStartIndex = findChecksumStartIndex(byteBuf);
        int checksumLength = byteBuf.writerIndex() - PACKAGE_END_LENGTH - checksumStartIndex;
        String checksum = byteBuf.toString(checksumStartIndex, checksumLength, US_ASCII);
        try {
            return OptionalInt.of(parseInt(checksum));
        } catch (NumberFormatException cause) {
            throw new InvalidChecksumException(cause);
        }
    }

    //TODO refactor
    @Override
    protected int calculateChecksum(ByteBuf byteBuf) {
        int checksumStartIndex = findChecksumStartIndex(byteBuf);
        int sum = 0;
        for (int i = byteBuf.readerIndex(); i < checksumStartIndex; i++) {
            sum += byteBuf.getUnsignedByte(i);
        }
        return sum;
    }

    //TODO refactor
    private static int findChecksumStartIndex(ByteBuf byteBuf) {
        int checksumBoundIndex = byteBuf.forEachByteDesc(
                byteBuf.readerIndex(), byteBuf.readableBytes(),
                value -> value != FIELD_SEPARATOR && value != PREFIX_BOUND
        );
        if (checksumBoundIndex == NO_SUCH_OCCURRENCE) {
            throw new InvalidChecksumException();
        }
        return checksumBoundIndex + 1;
    }
}
