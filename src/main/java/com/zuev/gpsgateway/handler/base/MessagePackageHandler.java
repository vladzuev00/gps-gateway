package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.exception.NotLoggedInTrackerException;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.DoNothing;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.Reply;
import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.service.MessageService;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.findTracker;
import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class MessagePackageHandler<PKG, MSG_SRC> extends PackageHandler<PKG> {
    private final MessageService messageService;

    public MessagePackageHandler(Class<PKG> packageType, MessageService messageService) {
        super(packageType);
        this.messageService = messageService;
    }

    @Override
    protected final CompletableFuture<HandlingResult> handleAsyncInternal(PKG pkg, ChannelHandlerContext context) {
        long trackerId = findTracker(context).orElseThrow(NotLoggedInTrackerException::new).id();
        List<Message> messages = getMessages(pkg, trackerId);
        if (messages.isEmpty()) {
            return reply(pkg);
        }
        return messageService.saveAllAsync(messages)
                .thenApplyAsync(
                        ignored -> {
                            if (!context.channel().isActive()) {
                                return new DoNothing();
                            }
                            Object response = createResponse(pkg);
                            return new Reply(response);
                        },
                        context.channel().eventLoop()
                );
    }

    protected abstract List<MSG_SRC> getMessageSources(PKG pkg);

    protected abstract Message toMessage(MSG_SRC source, long trackerId);

    protected abstract Object createResponse(PKG pkg);

    private List<Message> getMessages(PKG pkg, long trackerId) {
        return getMessageSources(pkg)
                .stream()
                .map(source -> toMessage(source, trackerId))
                .toList();
    }

    private CompletableFuture<HandlingResult> reply(PKG pkg) {
        Object response = createResponse(pkg);
        return completedFuture(new Reply(response));
    }
}
