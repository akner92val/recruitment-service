package com.mlavrenko.api.repository;

import com.mlavrenko.api.domain.Application;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ApplicationRepositoryTest {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Test
    void findAllByOfferId() {
        long id = 1;

        List<Application> allByOfferId = applicationRepository.findAllByOfferId(id);

        Assertions.assertThat(allByOfferId).isEmpty();
    }
}