package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.exception.NotLoggedInTrackerException;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.DoNothing;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.Reply;
import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.service.MessageService;
import com.zuev.gpsgateway.validator.MessageValidator;
import io.netty.channel.ChannelHandlerContext;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.*;
import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class MessagePackageHandler<PKG, MSG_SRC> extends PackageHandler<PKG> {
    private final MessageValidator messageValidator;
    private final MessageService messageService;

    public MessagePackageHandler(Class<PKG> packageType,
                                 MessageValidator messageValidator,
                                 MessageService messageService) {
        super(packageType);
        this.messageValidator = messageValidator;
        this.messageService = messageService;
    }

    @Override
    protected final CompletableFuture<HandlingResult> handleAsyncInternal(PKG pkg, ChannelHandlerContext context) {
        Tracker tracker = findTracker(context).orElseThrow(NotLoggedInTrackerException::new);
        long lastEpochMillis = findLastMessage(context)
                .map(Message::epochMillis)
                .orElse(Long.MIN_VALUE);
        List<Message> newMessages = getMessageSources(pkg)
                .stream()
                .map(source -> toMessage(source, tracker))
                .filter(messageValidator::isValid)
                .filter(message -> message.epochMillis() > lastEpochMillis)
                .toList();
        if (newMessages.isEmpty()) {
            Object response = createResponse(pkg);
            return completedFuture(new Reply(response));
        }
        return messageService.saveAllAsync(newMessages)
                .thenApplyAsync(
                        ignored -> {
                            if (!context.channel().isActive()) {
                                return new DoNothing();
                            }
                            Message newestMessage = newMessages.stream()
                                    .max(Comparator.comparingLong(Message::epochMillis))
                                    .orElseThrow();
                            if (
                                    newestMessage.epochMillis() > findLastMessage(context)
                                            .map(Message::epochMillis)
                                            .orElse(Long.MIN_VALUE)
                            ) {
                                setLastMessage(context, newestMessage);
                            }
                            Object response = createResponse(pkg);
                            return new Reply(response);
                        },
                        context.channel().eventLoop()
                );
    }

    protected abstract List<MSG_SRC> getMessageSources(PKG pkg);

    protected abstract Message toMessage(MSG_SRC source, Tracker tracker);

    protected abstract Object createResponse(PKG pkg);
}
