package com.zuev.gpsgateway.enricher;

import com.zuev.gpsgateway.model.Message;
import org.springframework.stereotype.Component;

//TODO implement and test
@Component
public class MessageEnricher {

    public Message enrich(Message message, Message previousMessage) {
        return message;
    }
}
