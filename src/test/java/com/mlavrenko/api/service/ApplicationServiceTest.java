package com.mlavrenko.api.service;


import com.mlavrenko.api.domain.Application;
import com.mlavrenko.api.domain.Offer;
import com.mlavrenko.api.domain.enums.ApplicationStatus;
import com.mlavrenko.api.dto.ApplicationDTO;
import com.mlavrenko.api.dto.OfferDTO;
import com.mlavrenko.api.repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
    private static final long ID = 1L;
    private ApplicationService applicationService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private OfferService offerService;

    @BeforeEach
    void before() {
        applicationService = new ApplicationService(applicationRepository, notificationService, offerService);
    }

    @Test
    @DisplayName("createApplication should save new instance of application in database and notify about status")
    void createApplication() {
        ApplicationDTO applicationDTO = getApplicationDTO();
        Application application = getApplication();
        Mockito.when(offerService.findById(ID)).thenReturn(Optional.of(new Offer()));
        Mockito.when(applicationRepository.save(Mockito.any(Application.class))).thenReturn(application);

        ApplicationDTO actual = applicationService.createApplication(applicationDTO);

        assertAll(
                () -> assertThat(actual).isEqualToIgnoringGivenFields(applicationDTO, "id", "offer"),
                () -> assertThat(actual.getOffer()).isEqualToComparingFieldByField(applicationDTO.getOffer()),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> Mockito.verify(applicationRepository).save(Mockito.any(Application.class)),
                () -> Mockito.verify(offerService).findById(ID),
                () -> Mockito.verify(notificationService)
                        .notifyStatusHasChanged(applicationDTO.getApplicationStatus(), applicationDTO.getId())

        );
    }

    private ApplicationDTO getApplicationDTO() {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(ID);
        dto.setCandidateEmail("email");
        dto.setResume("Resume");
        OfferDTO offer = new OfferDTO();
        offer.setId(ID);
        dto.setOffer(offer);
        dto.setApplicationStatus(ApplicationStatus.APPLIED);
        return dto;
    }

    private Application getApplication() {
        Application application = new Application();
        Offer offer = new Offer();
        offer.setId(ID);
        application.setOffer(offer);
        application.setId(ID);
        application.setResume("Resume");
        application.setCandidateEmail("email");
        return application;
    }

    @Test
    @DisplayName("updateApplication should update entity with notification if entity exists and status updated")
    void updateApplication() {
        ApplicationDTO applicationDTO = getApplicationDTO();
        Application expected = getApplication();
        applicationDTO.setApplicationStatus(ApplicationStatus.HIRED);
        Mockito.when(applicationRepository.findById(applicationDTO.getId())).thenReturn(Optional.of(expected));
        Mockito.when(applicationRepository.save(any(Application.class))).thenReturn(expected);
        Mockito.doNothing().when(notificationService).notifyStatusHasChanged(ApplicationStatus.HIRED, expected.getId());

        ApplicationDTO updated = applicationService.updateApplication(applicationDTO);


        assertAll(
                () -> assertThat(updated).isEqualToIgnoringGivenFields(applicationDTO, "offer"),
                () -> assertThat(updated.getOffer()).isEqualToComparingFieldByField(applicationDTO.getOffer()),
                () -> Mockito.verify(applicationRepository).findById(applicationDTO.getId()),
                () -> Mockito.verify(applicationRepository).save(Mockito.any(Application.class)),
                () -> Mockito.verify(notificationService)
                        .notifyStatusHasChanged(ApplicationStatus.HIRED, expected.getId())
        );
    }

    @Test
    @DisplayName("updateApplication should throw expected exception when application to update doesn't exist")
    void updateApplicationNotFound() {
        assertThrows(EntityNotFoundException.class, () -> applicationService.updateApplication(getApplicationDTO()));
    }

    @Test
    @DisplayName("updateApplication should throw expected exception when application to update doesn't exist")
    void updateApplicationOfferNotFound() {
        ApplicationDTO applicationDTO = getApplicationDTO();
        Application application = getApplication();
        application.setOffer(new Offer());
        Mockito.when(applicationRepository.findById(applicationDTO.getId())).thenReturn(Optional.of(application));

        assertThrows(IllegalArgumentException.class, () -> applicationService.updateApplication(applicationDTO));
    }

    @Test
    @DisplayName("getById should return expected dto when entity exists")
    void getById() {
        Application expected = getApplication();
        Mockito.when(applicationRepository.findById(ID)).thenReturn(Optional.of(expected));

        ApplicationDTO actual = applicationService.getById(ID);

        assertAll(
                () -> assertThat(expected).isEqualToIgnoringGivenFields(actual, "offer"),
                () -> assertThat(expected.getOffer()).isEqualToComparingFieldByField(actual.getOffer()),
                () -> Mockito.verify(applicationRepository).findById(ID)
        );
    }

    @Test
    @DisplayName("getById should throw expected exception when entity doesn't exist")
    void getByIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> applicationService.getById(ID));
    }

    @Test
    @DisplayName("findAllByOfferId should return expected applications when applications with given offer id exist")
    void findAllByOfferId() {
        List<Application> expected = Arrays.asList(new Application(), new Application());
        Mockito.when(applicationRepository.findAllByOfferId(ID)).thenReturn(expected);

        List<ApplicationDTO> applications = applicationService.getAllByOfferId(ID);

        assertAll(
                () -> assertThat(applications).hasSize(expected.size()),
                () -> Mockito.verify(applicationRepository).findAllByOfferId(ID)
        );
    }

    @Test
    @DisplayName("getApplicationCount should return expected count when repository returns count of entities")
    void getApplicationsCount() {
        long expected = 42;
        Mockito.when(applicationRepository.count()).thenReturn(expected);

        long count = applicationService.getApplicationsCount();

        assertAll(
                () -> assertThat(count).isEqualTo(expected),
                () -> Mockito.verify(applicationRepository).count()
        );
    }
}