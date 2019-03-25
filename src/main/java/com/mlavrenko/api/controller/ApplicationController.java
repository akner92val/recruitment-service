package com.mlavrenko.api.controller;

import com.mlavrenko.api.service.ApplicationService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "recruitment-service/", produces = MediaTypes.HAL_JSON_VALUE)
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }
}
