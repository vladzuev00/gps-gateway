package com.zuev.gpsgateway.decoder.cherry;

import com.zuev.gpsgateway.decoder.base.TextProtocolDecoder;
import com.zuev.gpsgateway.decoder.cherry.pkg.CherryPackageDecoder;
import com.zuev.gpsgateway.exception.InvalidChecksumException;
import com.zuev.gpsgateway.exception.TooLongPackageException;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Supplier;

import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.US_ASCII;

public final class CherryProtocolDecoder extends TextProtocolDecoder {
    static final Charset CHARSET = US_ASCII;
    static final byte PACKAGE_END = '\0';
    static final int NO_SUCH_OCCURRENCE = -1;
    static final int MAX_INCOMPLETE_PACKAGE_LENGTH = 64_000;
    static final byte FIELD_SEPARATOR = ';';
    static final byte PREFIX_END = '@';
    static final int PACKAGE_END_LENGTH = 1;

    public CherryProtocolDecoder(List<CherryPackageDecoder> packageDecoders) {
        super(packageDecoders, CHARSET);
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
        try {
            int checksumStartIndex = getChecksumStartIndexOrThrow(byteBuf, InvalidChecksumException::new);
            int checksumLength = byteBuf.writerIndex() - PACKAGE_END_LENGTH - checksumStartIndex;
            String checksum = byteBuf.toString(checksumStartIndex, checksumLength, CHARSET);
            return OptionalInt.of(parseInt(checksum));
        } catch (NumberFormatException exception) {
            throw new InvalidChecksumException();
        }
    }

    //TODO refactor
    @Override
    protected int calculateChecksum(ByteBuf byteBuf) {
        int checksumStartIndex = getChecksumStartIndexOrThrow(byteBuf, IllegalStateException::new);
        int sum = 0;
        for (int i = byteBuf.readerIndex(); i < checksumStartIndex; i++) {
            sum += byteBuf.getUnsignedByte(i);
        }
        return sum;
    }

    //TODO refactor
    private int getChecksumStartIndexOrThrow(ByteBuf byteBuf, Supplier<RuntimeException> exceptionSupplier) {
        int beforeChecksumIndex = byteBuf.indexOf(byteBuf.writerIndex(), byteBuf.readerIndex(), FIELD_SEPARATOR);
        if (beforeChecksumIndex == NO_SUCH_OCCURRENCE) {
            beforeChecksumIndex = byteBuf.indexOf(byteBuf.readerIndex() + 1, byteBuf.writerIndex(), PREFIX_END);
        }
        if (beforeChecksumIndex == NO_SUCH_OCCURRENCE) {
            throw exceptionSupplier.get();
        }
        return beforeChecksumIndex + 1;
    }
}
