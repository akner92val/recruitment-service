package com.mlavrenko.api.controller;

import com.mlavrenko.api.dto.ApplicationDTO;
import com.mlavrenko.api.service.ApplicationService;
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
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping(value = "/application", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationDTO> create(ApplicationDTO applicationDTO) {
        ApplicationDTO application = applicationService.createApplication(applicationDTO);

        application.add(linkTo(methodOn(ApplicationController.class).create(applicationDTO)).withSelfRel());
        URI uri = UriComponentsBuilder.fromHttpUrl(application.getRequiredLink(IanaLinkRelations.SELF).getHref()).build().toUri();

        return ResponseEntity.created(uri).body(application);
    }

    @GetMapping("/application/{id}")
    public ResponseEntity<ApplicationDTO> getById(@Valid @RequestBody Long id) {
        ApplicationDTO applicationDTO = applicationService.getById(id);
        return ResponseEntity.ok().body(applicationDTO);
    }

    @GetMapping("/applications/{offerId}}")
    public ResponseEntity<List<ApplicationDTO>> getAllByOfferId(Long offerId) {
        List<ApplicationDTO> applications = applicationService.getAllByOfferId(offerId);
        return ResponseEntity.ok().body(applications);
    }

    @GetMapping("/applications/count}")
    public ResponseEntity<Long> getApplicationsCount() {
        long count = applicationService.getApplicationsCount();
        return ResponseEntity.ok().body(count);
    }
}
