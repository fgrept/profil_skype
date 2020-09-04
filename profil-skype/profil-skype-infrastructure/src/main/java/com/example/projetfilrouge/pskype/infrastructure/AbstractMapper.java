package com.example.projetfilrouge.pskype.infrastructure;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Abstract implementation of a bean mapper
 *
 * @param <T> The source class
 * @param <S> The target class
 *
 * @author 472957
 */
public abstract class AbstractMapper<T, S> {

	/**
	 * @param entity entity
	 * @return the mapped entity
	 */
	public abstract T mapToDomain(S entity);

	/**
	 * @param dto dto
	 * @return the mapped entity
	 */
	public abstract S mapToEntity(T dto);

	/**
	 * @param entityList entityList
	 * @return a List of the mapped entity
	 */
	public List<T> mapToDomainList(final List<S> entityList) {
		return entityList.stream().filter(Objects::nonNull).map(this::mapToDomain).collect(Collectors.toList());
	}

	/**
	 * @param dtoList dtoList
	 * @return a List of the mapped entity
	 */
	public List<S> mapToEntityList(final List<T> dtoList) {
		return dtoList.stream().filter(Objects::nonNull).map(this::mapToEntity).collect(Collectors.toList());
	}

}