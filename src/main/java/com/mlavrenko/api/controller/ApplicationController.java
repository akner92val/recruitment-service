package com.mlavrenko.api.controller;

import com.mlavrenko.api.dto.ApplicationDTO;
import com.mlavrenko.api.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "recruitment-service/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping(value = "/application", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationDTO> create(@Valid @RequestBody ApplicationDTO applicationDTO) {
        ApplicationDTO application = applicationService.createApplication(applicationDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }

    @PutMapping(value = "/application", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationDTO> update(@Valid @RequestBody ApplicationDTO applicationDTO) {
        ApplicationDTO application = applicationService.createApplication(applicationDTO);

        return ResponseEntity.status(HttpStatus.OK).body(application);
    }

    @GetMapping("/application/{id}")
    public ResponseEntity<ApplicationDTO> getById(@Valid @RequestBody Long id) {
        ApplicationDTO applicationDTO = applicationService.getById(id);
        return ResponseEntity.ok().body(applicationDTO);
    }

    @GetMapping("/applications/{offerId}}")
    public ResponseEntity<List<ApplicationDTO>> getAllByOfferId(@PathVariable Long offerId) {
        List<ApplicationDTO> applications = applicationService.getAllByOfferId(offerId);
        return ResponseEntity.ok().body(applications);
    }

    @GetMapping("/applications/count}")
    public ResponseEntity<Long> getApplicationsCount() {
        long count = applicationService.getApplicationsCount();
        return ResponseEntity.ok().body(count);
    }
}
