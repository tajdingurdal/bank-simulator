package com.solid.soft.solid_soft_bank.mapper;

import com.solid.soft.solid_soft_bank.model.BaseEntity;
import com.solid.soft.solid_soft_bank.model.dto.BaseDTO;

import java.util.List;

public interface BaseMapper<D extends BaseDTO, E extends BaseEntity> {

    E toEntity(D dto);
    D toDto(E entity);
    List<D> toDto(List<E> entities);
}
