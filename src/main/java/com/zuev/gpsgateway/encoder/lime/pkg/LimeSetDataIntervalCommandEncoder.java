package com.zuev.gpsgateway.encoder.lime.pkg;

import com.zuev.gpsgateway.model.lime.LimeSetDataIntervalCommand;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public final class LimeSetDataIntervalCommandEncoder extends LimePackageEncoder<LimeSetDataIntervalCommand> {
    private static final int TAG_VALUE_PAIRS_LENGTH = 1 + Short.BYTES;
    private static final byte INTERVAL_TAG = 0x0C;

    public LimeSetDataIntervalCommandEncoder() {
        super(LimeSetDataIntervalCommand.class, TAG_VALUE_PAIRS_LENGTH);
    }

    @Override
    protected void putTagValuePairs(ByteBuffer buffer, LimeSetDataIntervalCommand pkg) {
        buffer.put(INTERVAL_TAG);
        buffer.putShort((short) pkg.seconds());
    }
}
