package com.mlavrenko.api.notification;

import com.mlavrenko.api.notification.events.StatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "notification.enabled", havingValue = "true")
public class EventSender {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @EventListener
    public void onApplicationRejected(StatusChangedEvent event) {
        onEvent(event);
    }

    private <T extends StatusChangedEvent> void onEvent(T event) {
        log.info(event.getEventMessage());
    }
}
