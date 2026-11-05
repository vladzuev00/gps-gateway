package com.zuev.gpsgateway.handler.base.pkg;

import com.zuev.gpsgateway.exception.NotLoggedInTrackerException;
import com.zuev.gpsgateway.handler.base.pkg.PackageHandler.HandlingResult.Reply;
import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.service.MessageService;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.findTracker;

public abstract class MessagePackageHandler<PKG, MSG_SRC> extends PackageHandler<PKG> {
    private final MessageService messageService;

    public MessagePackageHandler(Class<PKG> packageType, MessageService messageService) {
        super(packageType);
        this.messageService = messageService;
    }

    @Override
    protected final CompletableFuture<HandlingResult> handleAsyncInternal(PKG pkg, ChannelHandlerContext context) {
        long trackerId = getLoggedInTracker(context).id();
        List<Message> messages = getMessages(pkg, trackerId);
        return messageService.publishAllAsync(messages)
                .thenApplyAsync(
                        ignored -> {
                            Object response = createResponse(pkg);
                            return new Reply(response);
                        },
                        context.channel().eventLoop()
                );
    }

    protected abstract List<MSG_SRC> getMessageSources(PKG pkg);

    protected abstract Message toMessage(MSG_SRC source, long trackerId);

    protected abstract Object createResponse(PKG pkg);

    private Tracker getLoggedInTracker(ChannelHandlerContext context) {
        return findTracker(context).orElseThrow(NotLoggedInTrackerException::new);
    }

    private List<Message> getMessages(PKG pkg, long trackerId) {
        return getMessageSources(pkg)
                .stream()
                .map(source -> toMessage(source, trackerId))
                .toList();
    }
}
