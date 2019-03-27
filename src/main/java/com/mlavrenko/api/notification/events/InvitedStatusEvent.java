package com.mlavrenko.api.notification.events;

import com.mlavrenko.api.domain.enums.ApplicationStatus;

public class InvitedStatusEvent extends ConsoleStatusChangedEvent {
    public InvitedStatusEvent(Long applicationId) {
        super(ApplicationStatus.INVITED, applicationId);
    }
}
