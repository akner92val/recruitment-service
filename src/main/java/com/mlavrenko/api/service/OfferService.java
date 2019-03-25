package com.mlavrenko.api.service;

import com.mlavrenko.api.domain.Offer;
import com.mlavrenko.api.dto.OfferDTO;
import com.mlavrenko.api.repository.OfferRepository;
import org.springframework.beans.BeanUtils;
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
        Offer offer = convertToDomain(offerDTO);
        return convertToDTO(offerRepository.save(offer));
    }

    private OfferDTO convertToDTO(Offer offer) {
        if (offer == null) {
            return null;
        } else {
            OfferDTO offerDTO = new OfferDTO();
            BeanUtils.copyProperties(offer, offerDTO);
            return offerDTO;
        }
    }

    private Offer convertToDomain(OfferDTO offerDTO) {
        Offer offer = new Offer();
        BeanUtils.copyProperties(offerDTO, offer);
        return offer;
    }

    public OfferDTO getById(Long id) {
        return convertToDTO(offerRepository.getOne(id));
    }

    public List<OfferDTO> getAll() {
        return offerRepository.findAll().stream().map(this::convertToDTO).collect(toList());
    }
}
