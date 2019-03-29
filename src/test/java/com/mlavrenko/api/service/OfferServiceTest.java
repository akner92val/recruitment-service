package com.mlavrenko.api.service;

import com.mlavrenko.api.domain.Offer;
import com.mlavrenko.api.dto.OfferDTO;
import com.mlavrenko.api.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@DisplayName("Offer service test")
@ExtendWith(MockitoExtension.class)
class OfferServiceTest {
    private OfferService offerService;
    @Mock
    private OfferRepository offerRepository;

    @BeforeEach
    void before() {
        offerService = new OfferService(offerRepository);
    }

    @Test
    void createOffer() {
        OfferDTO offerDTO = new OfferDTO();
        Offer expected = new Offer();

        Mockito.when(offerRepository.save(any(Offer.class))).thenReturn(expected);

        OfferDTO actual = offerService.createOffer(offerDTO);

        Mockito.verify(offerRepository).save(any(Offer.class));
        assertThat(actual).isEqualTo(offerDTO);
    }

    @Test
    @DisplayName("findById returns empty optional when entity doesn't exist")
    void findByIdEmpty() {
        long id = 1L;
        Optional<Offer> offer = offerService.findById(id);

        assertAll(
                () -> Mockito.verify(offerRepository).findById(id),
                () -> assertThat(offer).isEmpty()
        );
    }

    @Test
    @DisplayName("findById returns expected entity when entity exists")
    void findById() {
        long id = 1L;
        Offer expected = new Offer();
        Mockito.when(offerRepository.getOne(id)).thenReturn(expected);
        Optional<Offer> offer = offerService.findById(id);

        assertAll(
                () -> Mockito.verify(offerRepository).findById(id),
                () -> assertThat(offer).isNotEmpty(),
                () -> assertThat(offer.get()).isEqualTo(expected)
        );
    }

    @Test
    @DisplayName("getById returns expected dto when entity exists")
    void getByIdFound() {
        long id = 1L;
        Offer offer = new Offer();
        Mockito.when(offerRepository.findById(id)).thenReturn(Optional.of(offer));

        OfferDTO offerDTO = offerService.getById(id);

        assertAll(
                () -> Mockito.verify(offerRepository).findById(id),
                () -> assertThat(offerDTO).isNotNull()
        );
    }

    @Test
    @DisplayName("getById throws not found exception when entity doesn't exist")
    void getByIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> offerService.findById(anyLong()));
    }


    @Test
    @DisplayName("getAll returns expected list of dtos")
    void getAll() {
        List<Offer> expected = Arrays.asList(new Offer(), new Offer());
        Mockito.when(offerRepository.findAll()).thenReturn(expected);

        List<OfferDTO> offers = offerService.getAll();

        Mockito.verify(offerRepository).findAll();
        assertThat(offers).hasSize(expected.size());
    }
}