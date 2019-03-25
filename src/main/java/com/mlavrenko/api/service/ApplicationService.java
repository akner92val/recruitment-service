package com.mlavrenko.api.service;

import com.mlavrenko.api.domain.Application;
import com.mlavrenko.api.dto.ApplicationDTO;
import com.mlavrenko.api.repository.ApplicationRepository;
import com.mlavrenko.api.utils.DTOConverter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public ApplicationDTO createApplication(ApplicationDTO offerDTO) {
        Application application = DTOConverter.convertToDomain(offerDTO, Application.class);
        return DTOConverter.convertToDTO(applicationRepository.save(application), ApplicationDTO.class);
    }

    public ApplicationDTO getById(Long id) {
        return DTOConverter.convertToDTO(applicationRepository.getOne(id), ApplicationDTO.class);
    }

    public List<ApplicationDTO> getAllByOfferId(Long offerId) {
        return applicationRepository.findAllByOfferId(offerId).stream()
                .map(application -> DTOConverter.convertToDTO(application, ApplicationDTO.class))
                .collect(Collectors.toList());
    }
}
