package com.mlavrenko.api.utils;

import org.springframework.beans.BeanUtils;

public class DTOConverter {
    private DTOConverter() {
    }

    public static <D, E> D convertToDTO(E entity, Class<D> dtoType) {
        if (entity == null) {
            return null;
        } else {
            D dto = BeanUtils.instantiateClass(dtoType);
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }
    }

    public static <D, E> E convertToDomain(D dto, Class<E> entityType) {
        E entity = BeanUtils.instantiateClass(entityType);
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
