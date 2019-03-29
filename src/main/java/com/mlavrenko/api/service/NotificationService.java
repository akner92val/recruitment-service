package com.mlavrenko.api.service;

import com.mlavrenko.api.domain.enums.ApplicationStatus;
import com.mlavrenko.api.notification.EventFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
class NotificationService {
    private final ApplicationEventPublisher eventPublisher;
    private final EventFactory eventFactory;

    NotificationService(ApplicationEventPublisher eventPublisher, EventFactory eventFactory) {
        this.eventPublisher = eventPublisher;
        this.eventFactory = eventFactory;
    }

    void notifyStatusHasChanged(ApplicationStatus newStatus, Long applicationId) {
        eventPublisher.publishEvent(eventFactory.createStatusChangedEvent(newStatus, applicationId));
    }
}
