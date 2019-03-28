package com.mlavrenko.api.controller;

import com.mlavrenko.api.dto.OfferDTO;
import com.mlavrenko.api.service.OfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.mlavrenko.api.utils.RestResponses.getOptionalEntityResponse;

@RestController
@RequestMapping(value = "/recruitment-service", produces = MediaType.APPLICATION_JSON_VALUE)
public class OfferController {
    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping(value = "/offer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OfferDTO> create(@Valid @RequestBody OfferDTO offerDTO) {
        OfferDTO created = offerService.createOffer(offerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/offer/{id}")
    public ResponseEntity<OfferDTO> getById(@PathVariable("id") long id) {
        OfferDTO offerDTO = offerService.getById(id);
        return getOptionalEntityResponse(offerDTO);
    }

    @GetMapping("/offers")
    public ResponseEntity<List<OfferDTO>> getAll() {
        List<OfferDTO> offers = offerService.getAll();
        return ResponseEntity.ok().body(offers);
    }
}
