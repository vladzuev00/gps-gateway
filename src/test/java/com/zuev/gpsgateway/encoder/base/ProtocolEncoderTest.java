package com.zuev.gpsgateway.encoder.base;

import com.zuev.gpsgateway.encoder.base.pkg.PackageEncoder;
import com.zuev.gpsgateway.exception.NoSuitableEncoderException;
import com.zuev.gpsgateway.util.ChannelAttributeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.findImei;
import static io.netty.buffer.ByteBufUtil.getBytes;
import static io.netty.buffer.Unpooled.buffer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ProtocolEncoderTest {

    @Mock
    private PackageEncoder<Object> mockedFirstPackageEncoder;

    @Mock
    private PackageEncoder<Object> mockedSecondPackageEncoder;

    @Mock
    private PackageEncoder<Object> mockedThirdPackageEncoder;

    private ProtocolEncoder encoder;

    @BeforeEach
    public void initializeEncoder() {
        encoder = new ProtocolEncoder(
                List.of(
                        mockedFirstPackageEncoder,
                        mockedSecondPackageEncoder,
                        mockedThirdPackageEncoder
                )
        );
    }

    @Test
    public void packageShouldBeEncoded() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class);
             LogCaptor givenLogCaptor = LogCaptor.forClass(ProtocolEncoder.class)) {
            ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
            TestPackage givenPackage = new TestPackage(5);
            ByteBuf givenOut = buffer();
            givenLogCaptor.setLogLevelToDebug();

            mockedChannelAttributeUtil.when(() -> findImei(same(givenContext)))
                    .thenReturn(Optional.of("00000355234055650192"));

            Channel givenChannel = mock(Channel.class);
            when(givenContext.channel()).thenReturn(givenChannel);
            when(givenChannel.toString()).thenReturn("test channel");

            when(mockedFirstPackageEncoder.isAbleEncode(same(givenPackage))).thenReturn(false);
            when(mockedSecondPackageEncoder.isAbleEncode(same(givenPackage))).thenReturn(true);
            when(mockedSecondPackageEncoder.encode(same(givenPackage))).thenReturn(new byte[]{1, 2, 3});

            encoder.encode(givenContext, givenPackage, givenOut);

            byte[] actual = getBytes(givenOut);
            byte[] expected = {1, 2, 3};
            assertArrayEquals(expected, actual);

            List<String> actualLogs = givenLogCaptor.getDebugLogs();
            List<String> expectedLogs = List.of("Encoding package TestPackage[value=5] on channel test channel, imei: 00000355234055650192");
            assertEquals(expectedLogs, actualLogs);

            verify(mockedFirstPackageEncoder, never()).encode(any());
            verifyNoInteractions(mockedThirdPackageEncoder);
        }
    }

    @Test
    public void packageShouldBeEncodedWithUnknownImei() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class);
             LogCaptor givenLogCaptor = LogCaptor.forClass(ProtocolEncoder.class)) {
            ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
            TestPackage givenPackage = new TestPackage(5);
            ByteBuf givenOut = buffer();
            givenLogCaptor.setLogLevelToDebug();

            mockedChannelAttributeUtil.when(() -> findImei(same(givenContext))).thenReturn(Optional.empty());

            Channel givenChannel = mock(Channel.class);
            when(givenContext.channel()).thenReturn(givenChannel);
            when(givenChannel.toString()).thenReturn("test channel");

            when(mockedFirstPackageEncoder.isAbleEncode(same(givenPackage))).thenReturn(false);
            when(mockedSecondPackageEncoder.isAbleEncode(same(givenPackage))).thenReturn(true);
            when(mockedSecondPackageEncoder.encode(same(givenPackage))).thenReturn(new byte[]{1, 2, 3});

            encoder.encode(givenContext, givenPackage, givenOut);

            byte[] actual = getBytes(givenOut);
            byte[] expected = {1, 2, 3};
            assertArrayEquals(expected, actual);

            List<String> actualLogs = givenLogCaptor.getDebugLogs();
            List<String> expectedLogs = List.of("Encoding package TestPackage[value=5] on channel test channel");
            assertEquals(expectedLogs, actualLogs);

            verify(mockedFirstPackageEncoder, never()).encode(any());
            verifyNoInteractions(mockedThirdPackageEncoder);
        }
    }

    @Test
    public void packageShouldNotBeEncodedBecauseOfNoSuitablePackageEncoder() {
        try (MockedStatic<ChannelAttributeUtil> mockedChannelAttributeUtil = mockStatic(ChannelAttributeUtil.class);
             LogCaptor givenLogCaptor = LogCaptor.forClass(ProtocolEncoder.class)) {
            ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
            TestPackage givenPackage = new TestPackage(5);
            ByteBuf givenOut = buffer();
            givenLogCaptor.setLogLevelToDebug();

            mockedChannelAttributeUtil.when(() -> findImei(same(givenContext))).thenReturn(Optional.empty());

            Channel givenChannel = mock(Channel.class);
            when(givenContext.channel()).thenReturn(givenChannel);
            when(givenChannel.toString()).thenReturn("test channel");

            when(mockedFirstPackageEncoder.isAbleEncode(same(givenPackage))).thenReturn(false);
            when(mockedSecondPackageEncoder.isAbleEncode(same(givenPackage))).thenReturn(false);
            when(mockedThirdPackageEncoder.isAbleEncode(same(givenPackage))).thenReturn(false);

            assertThrows(NoSuitableEncoderException.class, () -> encoder.encode(givenContext, givenPackage, givenOut));
            assertFalse(givenOut.isReadable());

            List<String> actualLogs = givenLogCaptor.getDebugLogs();
            List<String> expectedLogs = List.of("Encoding package TestPackage[value=5] on channel test channel");
            assertEquals(expectedLogs, actualLogs);

            verify(mockedFirstPackageEncoder, never()).encode(any());
            verify(mockedSecondPackageEncoder, never()).encode(any());
            verify(mockedThirdPackageEncoder, never()).encode(any());
        }
    }

    private record TestPackage(int value) {
    }
}
