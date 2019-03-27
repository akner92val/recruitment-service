package com.mlavrenko.api.notification.events;

import com.mlavrenko.api.domain.enums.ApplicationStatus;

public class RejectedStatusEvent extends ConsoleStatusChangedEvent {
    public RejectedStatusEvent(Long applicationId) {
        super(ApplicationStatus.REJECTED, applicationId);
    }
}
