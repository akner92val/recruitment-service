package com.mlavrenko.api.controller;

import com.mlavrenko.api.dto.ApplicationDTO;
import com.mlavrenko.api.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/recruitment-service/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping(value = "application", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationDTO create(@Valid @RequestBody ApplicationDTO applicationDTO) {
        return applicationService.createApplication(applicationDTO);
    }

    @PutMapping(value = "application", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApplicationDTO update(@Valid @RequestBody ApplicationDTO applicationDTO) {
        return applicationService.updateApplication(applicationDTO);
    }

    @GetMapping("application/{id}")
    public ApplicationDTO getById(@Valid @PathVariable("id") long id) {
        return applicationService.getById(id);
    }

    @GetMapping("applications/{offerId}")
    public List<ApplicationDTO> getAllByOfferId(@Valid @PathVariable("offerId") long offerId) {
        return applicationService.getAllByOfferId(offerId);
    }

    @GetMapping("application/count")
    public Long getApplicationsCount() {
        return applicationService.getApplicationsCount();
    }
}
