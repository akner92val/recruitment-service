package com.mlavrenko.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlavrenko.api.domain.Application;
import com.mlavrenko.api.domain.enums.ApplicationStatus;
import com.mlavrenko.api.dto.ApplicationDTO;
import com.mlavrenko.api.dto.OfferDTO;
import com.mlavrenko.api.repository.ApplicationRepository;
import com.mlavrenko.api.repository.OfferRepository;
import com.mlavrenko.api.service.OfferService;
import com.mlavrenko.api.utils.DTOConverter;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private OfferRepository offerRepository;
    @Autowired
    private OfferService offerService;

    @AfterEach
    void after() {
        applicationRepository.deleteAll();
        offerRepository.deleteAll();
    }

    @Test
    @DisplayName("create should create new entity and return status 201")
    void create() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ApplicationDTO application = getApplicationDTO();
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

    private ApplicationDTO getApplicationDTO() {
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
        return application;
    }

    @Test
    @DisplayName("update should return updated entity when entity exist")
    void update() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Application created = createApplication();
        created.setApplicationStatus(ApplicationStatus.HIRED);
        ApplicationDTO dto = DTOConverter.convertToDTO(created, ApplicationDTO.class);
        String value = objectMapper.writeValueAsString(dto);
        mvc.perform(put("/recruitment-service/application")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(value)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(dto.getId().intValue())))
                .andExpect(jsonPath("$.applicationStatus", equalTo(dto.getApplicationStatus().name())));
    }

    @Test
    @DisplayName("update should return status not found when entity doesn't exist")
    void updateNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ApplicationDTO application = getApplicationDTO();
        application.setId(1L);
        String value = objectMapper.writeValueAsString(application);
        mvc.perform(put("/recruitment-service/application")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(value)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("update should return status bad request when invalid entity used for update")
    void updateBadRequest() throws Exception {
        Application created = createApplication();
        ObjectMapper objectMapper = new ObjectMapper();
        ApplicationDTO dto = DTOConverter.convertToDTO(created, ApplicationDTO.class);
        OfferDTO offer = DTOConverter.convertToDTO(created.getOffer(), OfferDTO.class);
        offer.setId(offer.getId() + 1);
        dto.setOffer(offer);
        String value = objectMapper.writeValueAsString(dto);
        mvc.perform(put("/recruitment-service/application")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(value)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getById should return status ok when entity exists")
    void getById() throws Exception {
        Application application = createApplication();
        mvc.perform(get("/recruitment-service/application/{id}", application.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(application.getId().intValue())))
                .andExpect(jsonPath("$.resume", equalTo(application.getResume())))
                .andExpect(jsonPath("$.candidateEmail", equalTo(application.getCandidateEmail())));
    }

    private Application createApplication() throws Exception {
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
    @DisplayName("getAllByOfferId should return status ok and expected list when applications with given offer exist")
    void getAllByOfferId() throws Exception {
        Application application = createApplication();
        Long id = application.getOffer().getId();
        mvc.perform(get("/recruitment-service/applications/{offerId}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("getApplicationsCount should return expected count of applications and status ok")
    void getApplicationsCount() throws Exception {
        create();

        mvc.perform(get("/recruitment-service/application/count")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", equalTo(1)));
    }
}