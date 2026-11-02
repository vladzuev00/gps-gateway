package com.zuev.gpsgateway.enricher;

import com.zuev.gpsgateway.model.Message;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//TODO implement and test
@Component
public class MessageEnricher {

    /**
     * @param messages sorted by time
     * @return enriched messages in the same order
     */
    public List<Message> enrichAll(List<Message> messages, Message latestMessage) {
        List<Message> enrichedMessages = new ArrayList<>(messages.size());
        Message previousMessage = latestMessage;
        for (Message message : messages) {
            Message enrichedMessage = enrich(message, previousMessage);
            enrichedMessages.add(enrichedMessage);
            previousMessage = enrichedMessage;
        }
        return enrichedMessages;
    }

    /**
     * For tracker without previous messages: first message stays as is
     *
     * @param messages sorted by time
     * @return enriched messages in the same order
     */
    public List<Message> enrichAll(List<Message> messages) {
        if (messages.isEmpty()) {
            return List.of();
        }
        Message firstMessage = messages.getFirst();
        List<Message> enrichedMessages = new ArrayList<>(messages.size());
        enrichedMessages.add(firstMessage);
        enrichedMessages.addAll(enrichAll(messages.subList(1, messages.size()), firstMessage));
        return enrichedMessages;
    }

    private Message enrich(Message message, Message previousMessage) {
        return message;
    }
}
