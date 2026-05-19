package com.zuev.gpsgateway.decoder.base;

import com.zuev.gpsgateway.decoder.base.pkg.PackageDecoder;
import com.zuev.gpsgateway.exception.InvalidChecksumException;
import com.zuev.gpsgateway.exception.NoSuitableDecoderException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.OptionalInt;

@RequiredArgsConstructor
public abstract class ProtocolDecoder<PKG_SRC> extends ByteToMessageDecoder {
    private final List<? extends PackageDecoder<PKG_SRC>> packageDecoders;

    @Override
    protected final void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> out) {
        findCompletePackageEnd(byteBuf)
                .ifPresent(
                        packageEnd -> {
                            int packageLength = packageEnd - byteBuf.readerIndex() + 1;
                            ByteBuf packageSlice = byteBuf.readSlice(packageLength);
                            validateChecksum(packageSlice);
                            PKG_SRC packageSource = toPackageSource(packageSlice);
                            Object decodedPackage = findDecoder(packageSource).decode(packageSource);
                            out.add(decodedPackage);
                        }
                );
    }

    protected abstract OptionalInt findCompletePackageEnd(ByteBuf byteBuf);

    protected abstract PKG_SRC toPackageSource(ByteBuf byteBuf);

    protected abstract String toLogString(PKG_SRC packageSource);

    protected abstract OptionalInt getChecksum(ByteBuf byteBuf);

    protected abstract int calculateChecksum(byte[] bytes);

    private void validateChecksum(ByteBuf byteBuf) {
        getChecksum(byteBuf)
                .ifPresent(
                        checksum -> {
                            byte[] bytes = new byte[byteBuf.readableBytes()];
                            byteBuf.getBytes(byteBuf.readerIndex(), bytes);
                            int calculated = calculateChecksum(bytes);
                            if (checksum != calculated) {
                                throw new InvalidChecksumException("Checksum validation failed");
                            }
                        }
                );
    }

    private PackageDecoder<PKG_SRC> findDecoder(PKG_SRC packageSource) {
        return packageDecoders.stream()
                .filter(decoder -> decoder.isAbleDecode(packageSource))
                .findFirst()
                .orElseThrow(
                        () -> new NoSuitableDecoderException(
                                "No package decoder for: " + toLogString(packageSource)
                        )
                );
    }
}
