package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.encoder.base.pkg.PackageEncoder;

import java.nio.ByteBuffer;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

public abstract class LimePackageEncoder<PKG> extends PackageEncoder<PKG> {

    public LimePackageEncoder(Class<PKG> packageType) {
        super(packageType);
    }

    @Override
    protected final byte[] encodeInternal(PKG pkg) {
        int tagValuePairsLength = getTagValuePairsLength(pkg);
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES + tagValuePairsLength)
                .order(LITTLE_ENDIAN);
        buffer.putShort((short) tagValuePairsLength);
        putTagValuePairs(buffer, pkg);
        return buffer.array();
    }

    protected abstract int getTagValuePairsLength(PKG pkg);

    protected abstract void putTagValuePairs(ByteBuffer buffer, PKG pkg);
}
