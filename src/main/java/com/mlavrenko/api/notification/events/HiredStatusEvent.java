package com.mlavrenko.api.notification.events;

import com.mlavrenko.api.domain.enums.ApplicationStatus;

public class HiredStatusEvent extends ConsoleStatusChangedEvent {
    public HiredStatusEvent(Long applicationId) {
        super(ApplicationStatus.HIRED, applicationId);
    }
}
