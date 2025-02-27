package com.scheidbachmann.masterdata.mapper;

import java.util.List;

/**
 * @param <D>
 * @param <E>
 * @author KaouechHaythem
 */
public interface EntityMapper<D, E> {

  D toDto(E entity);

  E toEntity(D dto);

  List<D> toDtos(List<E> entities);
}
