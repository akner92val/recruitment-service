package com.mlavrenko.api.service;

import com.mlavrenko.api.domain.enums.ApplicationStatus;
import com.mlavrenko.api.notification.EventFactory;
import com.mlavrenko.api.notification.events.StatusChangedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Notification service test")
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    private NotificationService notificationService;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private EventFactory eventFactory;

    @BeforeEach
    void before() {
        notificationService = new NotificationService(eventPublisher, eventFactory);
    }

    @Test
    @DisplayName("should create status changed event and publish it when new status provided")
    void notifyStatusHasChanged() {
        long id = 1L;
        ApplicationStatus status = ApplicationStatus.APPLIED;
        StatusChangedEvent statusChangedEvent = Mockito.mock(StatusChangedEvent.class);
        Mockito.when(eventFactory.createStatusChangedEvent(status, id)).thenReturn(statusChangedEvent);

        notificationService.notifyStatusHasChanged(status, id);

        assertAll(
                () -> Mockito.verify(eventPublisher).publishEvent(statusChangedEvent),
                () -> Mockito.verify(eventFactory).createStatusChangedEvent(status, id)
        );
    }
}