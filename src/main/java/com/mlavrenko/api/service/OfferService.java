package com.mlavrenko.api.service;

import com.mlavrenko.api.domain.Offer;
import com.mlavrenko.api.dto.OfferDTO;
import com.mlavrenko.api.repository.OfferRepository;
import com.mlavrenko.api.utils.DTOConverter;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class OfferService {
    private final OfferRepository offerRepository;

    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public OfferDTO createOffer(OfferDTO offerDTO) {
        Offer offer = DTOConverter.convertToDomain(offerDTO, Offer.class);
        return DTOConverter.convertToDTO(offerRepository.save(offer), OfferDTO.class);
    }

    public OfferDTO getById(long id) {
        return findById(id)
                .map(offer -> DTOConverter.convertToDTO(offer, OfferDTO.class))
                .orElseThrow(EntityNotFoundException::new);
    }

    public Optional<Offer> findById(long id) {
        return offerRepository.findById(id);
    }

    public List<OfferDTO> getAll() {
        return offerRepository.findAll().stream()
                .map(offer -> DTOConverter.convertToDTO(offer, OfferDTO.class))
                .collect(toList());
    }
}
