package com.mlavrenko.api.notification.events;

import com.mlavrenko.api.domain.enums.ApplicationStatus;

public class AppliedStatusEvent extends ConsoleStatusChangedEvent {
    public AppliedStatusEvent(Long applicationId) {
        super(ApplicationStatus.APPLIED, applicationId);
    }
}
