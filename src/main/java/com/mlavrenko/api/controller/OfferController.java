package com.mlavrenko.api.controller;

import com.mlavrenko.api.dto.OfferDTO;
import com.mlavrenko.api.service.OfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/recruitment-service/", produces = MediaType.APPLICATION_JSON_VALUE)
public class OfferController {
    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping(value = "offer", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public OfferDTO create(@Valid @RequestBody OfferDTO offerDTO) {
        return offerService.createOffer(offerDTO);
    }

    @GetMapping("offer/{id}")
    public OfferDTO getById(@Valid @PathVariable("id") long id) {
        return offerService.getById(id);
    }

    @GetMapping("offers")
    public List<OfferDTO> getAll() {
        return offerService.getAll();
    }
}
