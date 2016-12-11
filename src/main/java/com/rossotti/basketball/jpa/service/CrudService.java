package com.rossotti.basketball.jpa.service;

import java.util.List;

public interface CrudService<T> {
	List<?> listAll();

	T getById(Long id);

	T create(T domainObject);

	T update(T domainObject);

	T delete(Long id);
}
