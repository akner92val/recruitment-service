package com.mlavrenko.api.service;

import com.mlavrenko.api.domain.Offer;
import com.mlavrenko.api.dto.OfferDTO;
import com.mlavrenko.api.repository.OfferRepository;
import com.mlavrenko.api.utils.DTOConverter;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OfferService {
    private final OfferRepository offerRepository;

    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public OfferDTO createOffer(OfferDTO offerDTO) {
        Offer offer = DTOConverter.convertToDomain(offerDTO, Offer.class);
        return DTOConverter.convertToDTO(offerRepository.save(offer), OfferDTO.class);
    }

    public OfferDTO getById(Long id) {
        return DTOConverter.convertToDTO(offerRepository.getOne(id), OfferDTO.class);
    }

    public List<OfferDTO> getAll() {
        return offerRepository.findAll().stream()
                .map(offer -> DTOConverter.convertToDTO(offer, OfferDTO.class))
                .collect(toList());
    }
}
