package com.mlavrenko.api.notification.events;

import com.mlavrenko.api.domain.enums.ApplicationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ConsoleStatusChangedEvent implements StatusChangedEvent {
    private final String message;


    public ConsoleStatusChangedEvent(ApplicationStatus status, Long applicationId) {
        message = String.format("Status of application id=%d has changed to: %s", applicationId, status);
    }

    @Override
    public String getEventMessage() {
        return message;
    }
}
