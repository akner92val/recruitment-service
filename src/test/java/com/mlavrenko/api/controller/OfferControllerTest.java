package com.mlavrenko.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlavrenko.api.domain.Offer;
import com.mlavrenko.api.dto.OfferDTO;
import com.mlavrenko.api.repository.OfferRepository;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

@DisplayName("Offer controller test")
@SpringBootTest
@AutoConfigureMockMvc
class OfferControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private OfferRepository offerRepository;

    @AfterEach
    void after() {
        offerRepository.deleteAll();
    }

    @Test
    @DisplayName("create should create new entity and return status 201")
    void create() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        OfferDTO offer = new OfferDTO();
        offer.setStartDate(DateUtil.now());
        offer.setJobTitle("Title");
        String value = objectMapper.writeValueAsString(offer);
        mvc.perform(post("/recruitment-service/offer")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(value)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.jobTitle", equalTo(offer.getJobTitle())))
                .andExpect(jsonPath("$.numberOfApplications", equalTo(0)));
    }

    @Test
    @DisplayName("getById should return status ok when entity exists")
    void getById() throws Exception {
        Offer entity = new Offer();
        entity.setJobTitle("job title");
        entity.setStartDate(DateUtil.now());
        Offer saved = offerRepository.save(entity);
        mvc.perform(get("/recruitment-service/offer/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(saved.getId().intValue())))
                .andExpect(jsonPath("$.jobTitle", equalTo(saved.getJobTitle())))
                .andExpect(jsonPath("$.startDate", notNullValue()))
                .andExpect(jsonPath("$.numberOfApplications", equalTo(saved.getNumberOfApplications())));
    }

    @Test
    @DisplayName("getById should return status not found when entity doesn't exist")
    void getByIdNotFound() throws Exception {
        mvc.perform(get("/recruitment-service/offer/{id}", 1234)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getAll should return expected list of offers and status ok")
    void getAllEmpty() throws Exception {
        mvc.perform(get("/recruitment-service/offers")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", empty()));
    }
}