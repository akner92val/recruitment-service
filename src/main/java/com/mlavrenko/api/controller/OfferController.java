package com.mlavrenko.api.controller;

import com.mlavrenko.api.dto.OfferDTO;
import com.mlavrenko.api.service.OfferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "recruitment-service/", produces = MediaTypes.HAL_JSON_VALUE)
public class OfferController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping(value = "/offer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OfferDTO> create(@Valid OfferDTO offerDTO) {
        OfferDTO created = offerService.createOffer(offerDTO);

        created.add(linkTo(methodOn(OfferController.class).create(offerDTO)).withSelfRel());
        URI uri = UriComponentsBuilder.fromHttpUrl(created.getRequiredLink(IanaLinkRelations.SELF).getHref()).build().toUri();

        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/offer/{id}")
    public ResponseEntity<OfferDTO> getById(@Valid @RequestBody Long id) {
        OfferDTO offerDTO = offerService.getById(id);
        return ResponseEntity.ok().body(offerDTO);
    }

    @GetMapping("/offers}")
    public ResponseEntity<List<OfferDTO>> getAll() {
        List<OfferDTO> offers = offerService.getAll();
        return ResponseEntity.ok().body(offers);
    }
}
