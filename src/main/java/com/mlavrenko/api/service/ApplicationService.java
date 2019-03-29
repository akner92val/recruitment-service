package com.mlavrenko.api.service;

import com.mlavrenko.api.domain.Application;
import com.mlavrenko.api.domain.Offer;
import com.mlavrenko.api.domain.enums.ApplicationStatus;
import com.mlavrenko.api.dto.ApplicationDTO;
import com.mlavrenko.api.dto.OfferDTO;
import com.mlavrenko.api.repository.ApplicationRepository;
import com.mlavrenko.api.utils.DTOConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.nio.channels.IllegalSelectorException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final NotificationService notificationService;
    private final OfferService offerService;


    public ApplicationService(ApplicationRepository applicationRepository, NotificationService notificationService, OfferService offerService) {
        this.applicationRepository = applicationRepository;
        this.notificationService = notificationService;
        this.offerService = offerService;
    }

    public ApplicationDTO createApplication(ApplicationDTO applicationDTO) {
        Application application = convertToDomain(applicationDTO);
        application.getOffer().incrementNumberOfApplications();
        Application saved = applicationRepository.save(application);

        notificationService.notifyStatusHasChanged(applicationDTO.getApplicationStatus(), saved.getId());

        return convertToDTO(saved);
    }

    private Application convertToDomain(ApplicationDTO applicationDTO) {
        Application application = DTOConverter.convertToDomain(applicationDTO, Application.class);
        long offerId = getOfferId(applicationDTO);
        Offer offer = offerService.findById(offerId).orElseThrow(IllegalArgumentException::new);
        application.setOffer(offer);
        return application;
    }

    private Long getOfferId(ApplicationDTO applicationDTO) {
        return Optional.ofNullable(applicationDTO.getOffer())
                .map(OfferDTO::getId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private ApplicationDTO convertToDTO(Application application) {
        ApplicationDTO dto = DTOConverter.convertToDTO(application, ApplicationDTO.class);
        dto.setOffer(DTOConverter.convertToDTO(application.getOffer(), OfferDTO.class));
        return dto;
    }

    public ApplicationDTO updateApplication(ApplicationDTO applicationDTO) {
        Application application = findById(applicationDTO.getId());
        ApplicationStatus oldStatus = application.getApplicationStatus();

        copyFromDto(applicationDTO, application);
        Application updated = applicationRepository.save(application);

        ApplicationStatus newStatus = updated.getApplicationStatus();
        if (oldStatus != newStatus) {
            notificationService.notifyStatusHasChanged(newStatus, updated.getId());
        }

        return convertToDTO(updated);
    }

    private void copyFromDto(ApplicationDTO applicationDTO, Application application) {
        BeanUtils.copyProperties(applicationDTO, application, "offer");
        updateOffer(application, applicationDTO.getOffer());
    }

    private void updateOffer(Application application, OfferDTO offer) {
        if (shouldUpdate(application, offer)) {
            Offer newOffer = offerService.findById(offer.getId()).orElseThrow(IllegalArgumentException::new);
            application.setOffer(newOffer);
        }
    }

    private boolean shouldUpdate(Application application, OfferDTO offer) {
        return Optional.ofNullable(offer)
                .map(OfferDTO::getId)
                .map(id -> !id.equals(application.getOffer().getId()))
                .orElse(false);
    }

    public ApplicationDTO getById(Long id) {
        Application application = findById(id);
        return convertToDTO(application);
    }

    private Application findById(Long id) {
        return applicationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<ApplicationDTO> getAllByOfferId(Long offerId) {
        return applicationRepository.findAllByOfferId(offerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long getApplicationsCount() {
        return applicationRepository.count();
    }
}
