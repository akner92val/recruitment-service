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

import javax.transaction.Transactional;
import java.util.List;
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
        Application saved = applicationRepository.save(application);

        notificationService.notifyStatusHasChanged(applicationDTO.getApplicationStatus(), saved.getId());

        return convertToDTO(saved);
    }

    private Application convertToDomain(ApplicationDTO applicationDTO) {
        Application application = DTOConverter.convertToDomain(applicationDTO, Application.class);
        Offer offer = offerService.getOfferById(applicationDTO.getOffer().getId());
        application.setOffer(offer);
        return application;
    }

    private ApplicationDTO convertToDTO(Application application) {
        ApplicationDTO dto = DTOConverter.convertToDTO(application, ApplicationDTO.class);
        dto.setOffer(DTOConverter.convertToDTO(application.getOffer(), OfferDTO.class));
        return dto;
    }

    public ApplicationDTO updateApplication(ApplicationDTO applicationDTO) {
        Application application = applicationRepository.getOne(applicationDTO.getId());
        if (application == null) {
            return null;
        }
        ApplicationStatus oldStatus = application.getApplicationStatus();

        BeanUtils.copyProperties(applicationDTO, application);
        Application updated = applicationRepository.save(application);

        ApplicationStatus newStatus = updated.getApplicationStatus();
        if (oldStatus != newStatus) {
            notificationService.notifyStatusHasChanged(newStatus, updated.getId());
        }

        return convertToDTO(updated);
    }

    public ApplicationDTO getById(Long id) {
        return convertToDTO(applicationRepository.getOne(id));
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
