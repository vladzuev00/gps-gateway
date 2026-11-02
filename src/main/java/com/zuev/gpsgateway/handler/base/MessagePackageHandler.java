package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.exception.NotLoggedInTrackerException;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.DoNothing;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.Reply;
import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.service.MessageService;
import io.netty.channel.ChannelHandlerContext;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.*;
import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class MessagePackageHandler<PKG, MSG_SRC> extends PackageHandler<PKG> {
    private final MessageService messageService;

    public MessagePackageHandler(Class<PKG> packageType, MessageService messageService) {
        super(packageType);
        this.messageService = messageService;
    }

    @Override
    protected final CompletableFuture<HandlingResult> handleAsyncInternal(PKG pkg, ChannelHandlerContext context) {
        Tracker tracker = findTracker(context).orElseThrow(NotLoggedInTrackerException::new);
        List<Message> newMessages = findNewMessages(pkg, tracker, context);
        if (newMessages.isEmpty()) {
            return completedFuture(createReply(pkg));
        }
        return messageService.saveAsync(newMessages)
                .thenApplyAsync(
                        ignored -> {
                            if (!context.channel().isActive()) {
                                return new DoNothing();
                            }
                            updateLastMessage(context, newMessages);
                            return createReply(pkg);
                        },
                        context.channel().eventLoop()
                );
    }

    protected abstract List<MSG_SRC> getMessageSources(PKG pkg);

    protected abstract Message mapToMessage(MSG_SRC source, Tracker tracker);

    protected abstract Object createResponse(PKG pkg);

    private List<Message> findNewMessages(PKG pkg, Tracker tracker, ChannelHandlerContext context) {
        long lastEpochMillis = findLastEpochMillis(context);
        return getMessageSources(pkg).stream()
                .map(source -> mapToMessage(source, tracker))
                .filter(message -> message.epochMillis() > lastEpochMillis)
                .toList();
    }

    private void updateLastMessage(ChannelHandlerContext context, List<Message> savedMessages) {
        Message newestMessage = savedMessages.stream()
                .max(Comparator.comparingLong(Message::epochMillis))
                .orElseThrow();
        if (newestMessage.epochMillis() > findLastEpochMillis(context)) {
            setLastMessage(context, newestMessage);
        }
    }

    private static long findLastEpochMillis(ChannelHandlerContext context) {
        return findLastMessage(context)
                .map(Message::epochMillis)
                .orElse(Long.MIN_VALUE);
    }

    private HandlingResult createReply(PKG pkg) {
        Object response = createResponse(pkg);
        return new Reply(response);
    }
}
