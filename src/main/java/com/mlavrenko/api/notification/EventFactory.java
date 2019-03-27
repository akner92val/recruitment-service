package com.mlavrenko.api.notification;

import com.mlavrenko.api.domain.enums.ApplicationStatus;
import com.mlavrenko.api.notification.events.AppliedStatusEvent;
import com.mlavrenko.api.notification.events.HiredStatusEvent;
import com.mlavrenko.api.notification.events.InvitedStatusEvent;
import com.mlavrenko.api.notification.events.RejectedStatusEvent;
import com.mlavrenko.api.notification.events.StatusChangedEvent;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class EventFactory {
    private final Map<ApplicationStatus, Function<Long, StatusChangedEvent>> events = new HashMap<>();

    public EventFactory() {
        events.put(ApplicationStatus.HIRED, HiredStatusEvent::new);
        events.put(ApplicationStatus.REJECTED, RejectedStatusEvent::new);
        events.put(ApplicationStatus.INVITED, InvitedStatusEvent::new);
        events.put(ApplicationStatus.APPLIED, AppliedStatusEvent::new);
    }

    public StatusChangedEvent createStatusChangedEvent(ApplicationStatus newStatus, Long applicationId) {
        return events.get(newStatus).apply(applicationId);
    }
}
