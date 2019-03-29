package com.mlavrenko.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlavrenko.api.domain.Application;
import com.mlavrenko.api.domain.enums.ApplicationStatus;
import com.mlavrenko.api.dto.ApplicationDTO;
import com.mlavrenko.api.dto.OfferDTO;
import com.mlavrenko.api.repository.ApplicationRepository;
import com.mlavrenko.api.service.OfferService;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Application controller test")
@SpringBootTest
@AutoConfigureMockMvc
class ApplicationControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private OfferService offerService;

    @Test
    @DisplayName("create should create new entity and return status 201")
    void create() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        OfferDTO offer = new OfferDTO();
        offer.setStartDate(DateUtil.now());
        offer.setJobTitle("Title");
        offer.setNumberOfApplications(1);
        OfferDTO createdOffer = offerService.createOffer(offer);

        ApplicationDTO application = new ApplicationDTO();
        application.setOffer(createdOffer);
        application.setApplicationStatus(ApplicationStatus.APPLIED);
        application.setCandidateEmail("email");
        application.setResume("resume");
        String value = objectMapper.writeValueAsString(application);
        mvc.perform(post("/recruitment-service/application")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(value)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.resume", equalTo(application.getResume())))
                .andExpect(jsonPath("$.candidateEmail", equalTo(application.getCandidateEmail())))
                .andExpect(jsonPath("$.applicationStatus", equalTo(application.getApplicationStatus().name())));
    }

    @Test
    void update() {
    }

    @Test
    @DisplayName("getById should return status ok when entity exists")
    void getById() throws Exception {
        Application application = getApplication();
        mvc.perform(get("/recruitment-service/application/{id}", application.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(application.getId().intValue())))
                .andExpect(jsonPath("$.resume", equalTo(application.getResume())))
                .andExpect(jsonPath("$.candidateEmail", equalTo(application.getCandidateEmail())));
    }

    private Application getApplication() throws Exception {
        create();
        return applicationRepository.findAll().stream()
                .findFirst()
                .orElseThrow(AssertionError::new);
    }

    @Test
    @DisplayName("getById should return status not found when entity doesn't exist")
    void getByIdNotFound() throws Exception {
        mvc.perform(get("/recruitment-service/application/{id}", 1234)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getById should return status not found when entity doesn't exist")
    void getAllByOfferId() {
    }

    @Test
    @DisplayName("getAllByOfferId should return status ok  and empty list when zero application was found")
    void getAllByOfferIdEmpty() throws Exception {
        mvc.perform(get("/recruitment-service/applications/{id}", 1234)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    @DisplayName("getApplicationsCount should return expected list of applications and status ok")
    void getApplicationsCount() throws Exception {
        applicationRepository.deleteAll();

        mvc.perform(get("/recruitment-service/applications/count")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", equalTo(0)));
    }
}