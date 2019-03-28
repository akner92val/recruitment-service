package com.mlavrenko.api.utils;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.function.Consumer;

public interface RestResponses {

    static URI toURI(Link link) {
        return UriComponentsBuilder.fromHttpUrl(link.getHref()).build().toUri();
    }


    static <T> ResponseEntity<T> getOptionalEntityResponse(T entity) {
        return getOptionalEntityResponse(entity, x -> {});
    }

    static <T> ResponseEntity<T> getOptionalEntityResponse(T entity, Consumer<T> whenExists) {
        return getResponseEntity(entity, HttpStatus.NOT_FOUND, whenExists);
    }

    static <T> ResponseEntity<T> postOptionalEntityResponse(T entity) {
        return getResponseEntity(entity, HttpStatus.CONFLICT);
    }

    static <T> ResponseEntity<T> getResponseEntity(T entity, HttpStatus httpStatus) {
        return getResponseEntity(entity, httpStatus, x -> {});
    }

    static <T> ResponseEntity<T> getResponseEntity(T entity, HttpStatus httpStatus, Consumer<T> whenExists) {
        if (entity == null) {
            return new ResponseEntity<>(httpStatus);
        } else {
            whenExists.accept(entity);
            return ResponseEntity.ok(entity);
        }
    }

    static <T, K> ResponseEntity<T> getOptionalEntityResponse(T entity, K parameter) {
        if (entity == null) {
            final HttpStatus status = parameter instanceof String ? HttpStatus.CONFLICT : HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(status);
        } else if (!parameter.equals(entity)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(entity);
        }
    }
}