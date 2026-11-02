package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.enricher.MessageEnricher;
import com.zuev.gpsgateway.exception.NotLoggedInTrackerException;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.DoNothing;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.Reply;
import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.service.MessageService;
import com.zuev.gpsgateway.validator.MessageValidator;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.*;
import static java.util.Comparator.comparingLong;
import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class MessagePackageHandler<PKG, MSG_SRC> extends PackageHandler<PKG> {
    private final MessageValidator messageValidator;
    private final MessageEnricher messageEnricher;
    private final MessageService messageService;

    public MessagePackageHandler(Class<PKG> packageType,
                                 MessageValidator messageValidator,
                                 MessageEnricher messageEnricher,
                                 MessageService messageService) {
        super(packageType);
        this.messageValidator = messageValidator;
        this.messageEnricher = messageEnricher;
        this.messageService = messageService;
    }

    @Override
    protected final CompletableFuture<HandlingResult> handleAsyncInternal(PKG pkg, ChannelHandlerContext context) {
        long trackerId = findTracker(context).orElseThrow(NotLoggedInTrackerException::new).id();
        List<Message> sortedValidMessages = getSortedValidMessages(pkg, trackerId);
        if (sortedValidMessages.isEmpty()) {
            return reply(pkg);
        }

        List<Message> enrichedMessages = findLatestMessage(context)
                .map(latestMessage -> messageEnricher.enrichAll(sortedValidMessages, latestMessage))
                .orElseGet(() -> messageEnricher.enrichAll(sortedValidMessages));
        return messageService.saveAllAsync(enrichedMessages)
                .thenApplyAsync(
                        ignored -> {
                            if (!context.channel().isActive()) {
                                return new DoNothing();
                            }
                            Message newestMessage = enrichedMessages.getLast();
                            if (
                                    newestMessage.epochMillis() > findLatestMessage(context)
                                            .map(Message::epochMillis)
                                            .orElse(Long.MIN_VALUE)
                            ) {
                                setLatestMessage(context, newestMessage);
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

    private List<Message> getSortedValidMessages(PKG pkg, long trackerId) {
        return getMessageSources(pkg)
                .stream()
                .map(source -> toMessage(source, trackerId))
                .filter(messageValidator::isValid)
                .sorted(comparingLong(Message::epochMillis))
                .toList();
    }

    private CompletableFuture<HandlingResult> reply(PKG pkg) {
        Object response = createResponse(pkg);
        return completedFuture(new Reply(response));
    }
}
