package com.mlavrenko.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlavrenko.api.domain.Offer;
import com.mlavrenko.api.dto.OfferDTO;
import com.mlavrenko.api.repository.OfferRepository;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OfferControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private OfferRepository offerRepository;

    @Test
    void create() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        OfferDTO offer = new OfferDTO();
        offer.setStartDate(DateUtil.now());
        offer.setJobTitle("Title");
        offer.setNumberOfApplications(1);
        String value = objectMapper.writeValueAsString(offer);
        mvc.perform(post("/recruitment-service/offer")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(value)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", comparesEqualTo(1)));
    }

    @Test
    void getByIdNotFound() throws Exception {
        mvc.perform(get("/recruitment-service/offer/{id}", 1234)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllEmpty() throws Exception {
        offerRepository.deleteAll();

        mvc.perform(get("/recruitment-service/offers")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", empty()));
    }
}