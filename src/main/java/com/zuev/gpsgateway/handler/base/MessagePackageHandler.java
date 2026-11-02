package com.zuev.gpsgateway.handler.base;

import com.zuev.gpsgateway.enricher.MessageEnricher;
import com.zuev.gpsgateway.exception.NotLoggedInTrackerException;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.DoNothing;
import com.zuev.gpsgateway.handler.base.PackageHandler.HandlingResult.Reply;
import com.zuev.gpsgateway.model.Message;
import com.zuev.gpsgateway.model.Tracker;
import com.zuev.gpsgateway.service.MessageService;
import com.zuev.gpsgateway.validator.MessageValidator;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.zuev.gpsgateway.util.ChannelAttributeUtil.*;
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
        Tracker tracker = findTracker(context).orElseThrow(NotLoggedInTrackerException::new);
        List<Message> validMessages = getMessageSources(pkg)
                .stream()
                .map(source -> toMessage(source, tracker))
                .filter(messageValidator::isValid)
                .sorted(Comparator.comparingLong(Message::epochMillis))
                .toList();
        if (validMessages.isEmpty()) {
            Object response = createResponse(pkg);
            return completedFuture(new Reply(response));
        }
        List<Message> enrichedMessages = new ArrayList<>(validMessages.size());
        Optional<Message> optionalPreviousMessage = findLastMessage(context);
        for (Message message : validMessages) {
            Message enrichedMessage = optionalPreviousMessage
                    .map(previousMessage -> messageEnricher.enrich(message, previousMessage))
                    .orElse(message);
            enrichedMessages.add(enrichedMessage);
            optionalPreviousMessage = Optional.of(enrichedMessage);
        }
        return messageService.saveAllAsync(enrichedMessages)
                .thenApplyAsync(
                        ignored -> {
                            if (!context.channel().isActive()) {
                                return new DoNothing();
                            }
                            Message newestMessage = enrichedMessages.getLast();
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
