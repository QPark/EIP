/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.core.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.jaxb.Marshalling;
import com.qpark.eip.core.jaxb.validation.Validator;
import com.qpark.eip.model.common.EntityValidationError;
import com.samples.platform.model.library.LibraryType;

/**
 * @author bhausen
 */
public class EntityValidator {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(EntityValidator.class);
	/** The {@link Validator}. */
	@Autowired
	private Validator validator;

	/**
	 * @param entity
	 *            the entity to validate.
	 * @param name
	 *            the name of the entity.
	 * @param uuid
	 *            the UUID of the entity.
	 * @return the {@link Optional} {@link EntityValidationError}.
	 */
	private Optional<EntityValidationError> validate(final Object entity,
			final String name, final String uuid) {
		EntityValidationError value = new EntityValidationError();
		value.setName(name);
		value.setUUID(uuid);
		List<String> errors = new ArrayList<>();
		try {
			this.validator.validate(entity);
		} catch (Exception e) {
			errors.add(e.getMessage());
		}
		value.getValidationError().addAll(errors);
		if (value.getValidationError().size() > 0) {
			return Optional.of(value);
		}
		return Optional.empty();
	}

	/**
	 * @param lib
	 *            the {@link LibraryType}.
	 * @return the list of {@link EntityValidationError}.
	 */
	public List<EntityValidationError> validateLibraryType(
			final LibraryType lib) {
		this.logger.debug("+validateLibraryType");
		List<EntityValidationError> value = new ArrayList<>();
		EntityValidationError general = this
				.validate(lib, lib.getName(), lib.getUUID())
				.orElse(new EntityValidationError());
		if (general.getValidationError().size() > 0) {
			try {
				LibraryType baseLibrary = Marshalling.deepCopyJAXB(lib,
						LibraryType.class);
				baseLibrary.getBooks().clear();
				this.validate(baseLibrary, lib.getName(), lib.getUUID())
						.ifPresent(e -> value.add(e));
				lib.getBooks().stream().forEach(o -> {
					this.validate(o, o.getTitle(), o.getUUID())
							.ifPresent(e -> value.add(e));
				});
			} catch (JAXBException e) {
				EntityValidationError error = new EntityValidationError();
				error.setName(lib.getName());
				error.setUUID(lib.getUUID());
				error.getValidationError().add(e.getMessage());
				error.getValidationError().addAll(general.getValidationError());
				value.add(error);
			}
		}
		this.logger.debug("-validateLibraryType");
		return value;
	}
}
