package com.mlavrenko.api.service;

import com.mlavrenko.api.domain.Application;
import com.mlavrenko.api.domain.enums.ApplicationStatus;
import com.mlavrenko.api.dto.ApplicationDTO;
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


    public ApplicationService(ApplicationRepository applicationRepository, NotificationService notificationService) {
        this.applicationRepository = applicationRepository;
        this.notificationService = notificationService;
    }

    public ApplicationDTO createApplication(ApplicationDTO applicationDTO) {
        Application application = DTOConverter.convertToDomain(applicationDTO, Application.class);
        application.getOffer().incrementNumberOfApplications();
        Application saved = applicationRepository.save(application);

        notificationService.notifyStatusHasChanged(saved.getApplicationStatus(), saved.getId());

        return DTOConverter.convertToDTO(saved, ApplicationDTO.class);
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

        return DTOConverter.convertToDTO(updated, ApplicationDTO.class);
    }

    public ApplicationDTO getById(Long id) {
        return DTOConverter.convertToDTO(applicationRepository.getOne(id), ApplicationDTO.class);
    }

    public List<ApplicationDTO> getAllByOfferId(Long offerId) {
        return applicationRepository.findAllByOfferId(offerId).stream()
                .map(application -> DTOConverter.convertToDTO(application, ApplicationDTO.class))
                .collect(Collectors.toList());
    }

    public long getApplicationsCount() {
        return applicationRepository.count();
    }
}
