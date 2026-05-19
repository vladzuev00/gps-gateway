package com.zuev.gpsgateway.decoder.base;

import com.zuev.gpsgateway.decoder.base.pkg.PackageDecoder;
import com.zuev.gpsgateway.exception.InvalidChecksumException;
import com.zuev.gpsgateway.exception.NoSuitableDecoderException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.OptionalInt;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ProtocolDecoderTest {

    @Mock
    private PackageDecoder<String> firstMockedPackageDecoder;

    @Mock
    private PackageDecoder<String> secondMockedPackageDecoder;

    @Mock
    private PackageDecoder<String> thirdMockedPackageDecoder;

    private EmbeddedChannel channel;

    @BeforeEach
    public void initializeProtocolDecoder() {
        channel = new EmbeddedChannel(
                new TestProtocolDecoder(
                        List.of(
                                firstMockedPackageDecoder,
                                secondMockedPackageDecoder,
                                thirdMockedPackageDecoder
                        )
                )
        );
    }

    @Test
    public void packageShouldBeDecoded() {
        ByteBuf givenByteBuf = wrappedBuffer(
                new byte[]{
                        'f', 'i', 'r', 's', 't', '-', 'p', 'a', 'c', 'k', 0x03, (byte) 0xF4, '\n',
                        's', 'e', 'c', 'o', 'n', 'd', 'p', 'a', 'c', 'k'
                }
        );

        when(firstMockedPackageDecoder.isAbleDecode("first-pack\u0003\uFFFD\n")).thenReturn(false);
        when(secondMockedPackageDecoder.isAbleDecode("first-pack\u0003\uFFFD\n")).thenReturn(true);

        Object givenDecodedPackage = new Object();
        when(secondMockedPackageDecoder.decode("first-pack\u0003\uFFFD\n")).thenReturn(givenDecodedPackage);

        assertTrue(channel.writeInbound(givenByteBuf));
        Object actual = channel.readInbound();
        assertSame(givenDecodedPackage, actual);
        assertEquals(13, givenByteBuf.readerIndex());

        verify(firstMockedPackageDecoder, never()).decode(any());
        verifyNoInteractions(thirdMockedPackageDecoder);
    }

    @Test
    public void incompletePackageShouldNotBeDecoded() {
        ByteBuf givenByteBuf = wrappedBuffer("first-package".getBytes(UTF_8));

        assertFalse(channel.writeInbound(givenByteBuf));
        assertEquals(0, givenByteBuf.readerIndex());

        verifyNoInteractions(firstMockedPackageDecoder, secondMockedPackageDecoder, thirdMockedPackageDecoder);
    }

    @Test
    public void packageShouldNotBeDecodedBecauseOfNoSuitableDecoder() {
        ByteBuf givenByteBuf = wrappedBuffer(
                new byte[]{
                        'f', 'i', 'r', 's', 't', '-', 'p', 'a', 'c', 'k', 0x03, (byte) 0xF4, '\n',
                        's', 'e', 'c', 'o', 'n', 'd', 'p', 'a', 'c', 'k'
                }
        );

        when(firstMockedPackageDecoder.isAbleDecode("first-pack\u0003\uFFFD\n")).thenReturn(false);
        when(secondMockedPackageDecoder.isAbleDecode("first-pack\u0003\uFFFD\n")).thenReturn(false);
        when(thirdMockedPackageDecoder.isAbleDecode("first-pack\u0003\uFFFD\n")).thenReturn(false);

        DecoderException actual = assertThrows(DecoderException.class, () -> channel.writeInbound(givenByteBuf));
        assertInstanceOf(NoSuitableDecoderException.class, actual.getCause());
        assertEquals(13, givenByteBuf.readerIndex());

        verify(firstMockedPackageDecoder, never()).decode(any());
        verify(secondMockedPackageDecoder, never()).decode(any());
        verify(thirdMockedPackageDecoder, never()).decode(any());
    }

    @Test
    public void packageShouldNotBeDecodedBecauseOfNotValidChecksum() {
        ByteBuf givenByteBuf = wrappedBuffer(
                new byte[]{
                        'f', 'i', 'r', 's', 't', '-', 'p', 'a', 'c', 'k', 0x03, (byte) 0xF5, '\n',
                        's', 'e', 'c', 'o', 'n', 'd', 'p', 'a', 'c', 'k'
                }
        );

        DecoderException actual = assertThrows(DecoderException.class, () -> channel.writeInbound(givenByteBuf));
        assertInstanceOf(InvalidChecksumException.class, actual.getCause());
        assertEquals(13, givenByteBuf.readerIndex());

        verifyNoInteractions(firstMockedPackageDecoder, secondMockedPackageDecoder, thirdMockedPackageDecoder);
    }

    private static final class TestProtocolDecoder extends ProtocolDecoder<String> {
        private static final byte PACKAGE_END = '\n';
        private static final int CHECKSUM_LENGTH = 2;
        private static final int PACKAGE_END_LENGTH = 1;

        public TestProtocolDecoder(List<PackageDecoder<String>> packageDecoders) {
            super(packageDecoders);
        }

        @Override
        protected OptionalInt findCompletePackageEnd(ByteBuf byteBuf) {
            int index = byteBuf.indexOf(byteBuf.readerIndex(), byteBuf.writerIndex(), PACKAGE_END);
            return index != -1 ? OptionalInt.of(index) : OptionalInt.empty();
        }

        @Override
        protected String toPackageSource(ByteBuf byteBuf) {
            return byteBuf.toString(UTF_8);
        }

        @Override
        protected String toLogString(String text) {
            return text;
        }

        @Override
        protected OptionalInt getChecksum(ByteBuf byteBuf) {
            return OptionalInt.of(byteBuf.getUnsignedShort(byteBuf.writerIndex() - CHECKSUM_LENGTH - PACKAGE_END_LENGTH));

        }

        @Override
        protected int calculateChecksum(byte[] bytes) {
            int sum = 0;
            for (int i = 0; i < bytes.length - CHECKSUM_LENGTH - PACKAGE_END_LENGTH; i++) {
                sum += bytes[i] & 0xFF;
            }
            return sum & 0xFFFF;
        }
    }
}
