package com.mlavrenko.api.service;


import com.mlavrenko.api.domain.Application;
import com.mlavrenko.api.domain.Offer;
import com.mlavrenko.api.domain.enums.ApplicationStatus;
import com.mlavrenko.api.dto.ApplicationDTO;
import com.mlavrenko.api.repository.ApplicationRepository;
import com.mlavrenko.api.utils.DTOConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
    private ApplicationService applicationService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private ApplicationRepository applicationRepository;

    @BeforeEach
    void setUp() {
        applicationService = new ApplicationService(applicationRepository, notificationService);
    }

    @Test
    void createApplication() {
        ApplicationDTO applicationDTO = getApplicationDTO();
        Application application = DTOConverter.convertToDomain(applicationDTO, Application.class);
        Mockito.when(applicationRepository.save(Mockito.any(Application.class))).thenReturn(application);

        ApplicationDTO actual = applicationService.createApplication(applicationDTO);

        assertThat(actual).isEqualToIgnoringGivenFields(applicationDTO);
        Mockito.verify(applicationRepository).save(Mockito.any(Application.class));
        Mockito.verify(notificationService)
                .notifyStatusHasChanged(applicationDTO.getApplicationStatus(), applicationDTO.getId());
    }

    private ApplicationDTO getApplicationDTO() {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(1L);
        dto.setCandidateEmail("email");
        dto.setResume("resume");
        dto.setOffer(new Offer());
        return dto;
    }

    @Test
    void updateApplication() {
        ApplicationDTO applicationDTO = getApplicationDTO();
        Application expected = DTOConverter.convertToDomain(applicationDTO, Application.class);
        Application application = new Application();
        application.setApplicationStatus(applicationDTO.getApplicationStatus());
        Mockito.when(applicationRepository.getOne(applicationDTO.getId())).thenReturn(application);
        Mockito.when(applicationRepository.save(any(Application.class))).thenReturn(expected);

        ApplicationDTO updated = applicationService.updateApplication(applicationDTO);

        assertThat(updated).isEqualTo(applicationDTO);
        Mockito.verify(applicationRepository).getOne(anyLong());
        Mockito.verify(applicationRepository).save(any(Application.class));
        Mockito.verifyZeroInteractions(notificationService);
    }

    @Test
    void getById() {
        long id = 1;
        Application expected = getApplication();
        expected.setId(id);
        Mockito.when(applicationRepository.getOne(id)).thenReturn(expected);

        ApplicationDTO actual = applicationService.getById(id);

        assertThat(expected).isEqualToComparingFieldByField(actual);
        Mockito.verify(applicationRepository).getOne(id);
    }

    private Application getApplication() {
        Application application = new Application();
        application.setOffer(new Offer());
        application.setResume("Resume");
        application.setCandidateEmail("email");
        application.setApplicationStatus(ApplicationStatus.APPLIED);
        return application;
    }

    @Test
    void getAllByOfferId() {
        long offerId = 42L;
        List<Application> expected = Arrays.asList(new Application(), new Application());
        Mockito.when(applicationRepository.findAllByOfferId(offerId)).thenReturn(expected);

        List<ApplicationDTO> applications = applicationService.getAllByOfferId(offerId);

        assertThat(applications.size()).isEqualTo(expected.size());
        Mockito.verify(applicationRepository).findAllByOfferId(offerId);
    }

    @Test
    void getApplicationsCount() {
        long expected = 42;
        Mockito.when(applicationRepository.count()).thenReturn(expected);

        long count = applicationService.getApplicationsCount();

        assertThat(count).isEqualTo(expected);
        Mockito.verify(applicationRepository).count();
    }

}