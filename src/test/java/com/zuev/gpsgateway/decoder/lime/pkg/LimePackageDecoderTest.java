package com.zuev.gpsgateway.decoder.lime.pkg;

import io.netty.buffer.ByteBuf;

import java.util.Map;
import java.util.Set;

public final class LimePackageDecoderTest {

    private static final class TestTimePackageDecoder extends LimePackageDecoder {
        private static final

        public TestTimePackageDecoder() {
            //TODO сделать все теги
            super(Set.of());
        }

        @Override
        protected Object decodeValueSlices(Map<Byte, ByteBuf> slicesByTags) {
            return null;
        }
    }
}
