package com.mlavrenko.api.repository;

import com.mlavrenko.api.domain.Application;
import com.mlavrenko.api.domain.Offer;
import com.mlavrenko.api.domain.enums.ApplicationStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest
class ApplicationRepositoryTest {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Test
    void findAllByOfferIdEmpty() {
        long id = 1;

        List<Application> allByOfferId = applicationRepository.findAllByOfferId(id);

        Assertions.assertThat(allByOfferId).isEmpty();
    }

    @Test
    void findAllByOfferIdNotEmpty() {
        Offer offer = getOffer();
        Offer another = getOffer();
        List<Application> applications = IntStream.range(0, 10)
                .mapToObj(i -> Stream.of(getApplication(offer), getApplication(another)))
                .flatMap(Function.identity())
                .collect(Collectors.toList());
        applicationRepository.saveAll(applications);

        Long id = applicationRepository.findAll().stream().findAny().map(Application::getOffer).map(Offer::getId).orElse(1L);

        List<Application> allByOfferId = applicationRepository.findAllByOfferId(id);

        assertThat(allByOfferId).hasSize(10);
        allByOfferId.forEach(application -> assertThat(application.getOffer().getId()).isEqualTo(id));
    }

    private Offer getOffer() {
        Offer offer = new Offer();
        offer.setJobTitle(UUID.randomUUID().toString());
        offer.setStartDate(Date.valueOf(LocalDate.now()));
        return offer;
    }

    private Application getApplication(Offer offer) {
        Application application = new Application();
        application.setOffer(offer);
        application.setResume("Resume");
        application.setCandidateEmail(UUID.randomUUID().toString());
        application.setApplicationStatus(ApplicationStatus.APPLIED);
        return application;
    }
}