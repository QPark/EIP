/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.serviceprovider.library.internal.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qpark.eip.model.common.EntityType_;
import com.samples.platform.model.library.BookType;
import com.samples.platform.model.library.BookType_;
import com.samples.platform.persistenceconfig.PersistenceConfig;

/**
 * @author bhausen
 */
@Repository
public class PlatformDao {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PlatformDao.class);
	/** The {@link EntityManager}. */
	@PersistenceContext(unitName = PersistenceConfig.PERSISTENCE_UNIT_NAME,
			name = PersistenceConfig.ENTITY_MANAGER_FACTORY_NAME)
	private EntityManager em;

	/**
	 * @param value
	 *                  the {@link BookType} to create.
	 * @return the created {@link BookType}.
	 */
	@Transactional(value = PersistenceConfig.TRANSACTION_MANAGER_NAME, propagation = Propagation.REQUIRED)
	public BookType createBook(final BookType value) {
		if (value != null) {
			try {
				this.em.persist(value);
				this.logger.debug("createBook id=" + value.getUUID());
			} catch (RuntimeException e) {
				e.printStackTrace();
				throw e;
			}
			return value;
		} else {
			this.logger.debug("createBook: value is null.");
			return null;
		}
	}

	/**
	 * @param value
	 *                  the id of the book to delete.
	 */
	@Transactional(value = PersistenceConfig.TRANSACTION_MANAGER_NAME, propagation = Propagation.REQUIRED)
	public void deleteBook(final BookType value) {
		if (value != null) {
			try {
				this.logger.debug("deleteBook id=" + value.getUUID());
				this.em.remove(value);
			} catch (RuntimeException e) {
				e.printStackTrace();
				throw e;
			}
		} else {
			this.logger.debug("deleteBook: value not found to delete it.");
		}
	}

	/**
	 * @param uuid
	 *                 the id of the book to find.
	 * @return the {@link BookType}.
	 */
	@Transactional(value = PersistenceConfig.TRANSACTION_MANAGER_NAME, propagation = Propagation.REQUIRED)
	public BookType getBookById(final String uuid) {
		BookType m = null;
		if (uuid == null) {
			this.logger.debug("getBookById: UUID is null.");
		} else {
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaQuery<BookType> q = cb.createQuery(BookType.class);
			Root<BookType> c = q.from(BookType.class);
			q.where(cb.equal(c.<String> get(EntityType_.UUID), uuid));
			TypedQuery<BookType> typedQuery = this.em.createQuery(q);
			try {
				m = typedQuery.getSingleResult();
				this.logger.debug("getBookByISBN: " + ToStringBuilder.reflectionToString(m));
			} catch (NoResultException e) {
				this.logger.debug("getBookByISBN: non value found for id=" + uuid);
				m = null;
			}
		}
		return m;
	}

	/**
	 * @param ISBN
	 *                 the ISBN of the book to find.
	 * @return the {@link BookType}.
	 */
	@Transactional(value = PersistenceConfig.TRANSACTION_MANAGER_NAME, propagation = Propagation.REQUIRED)
	public BookType getBookByISBN(final String ISBN) {
		BookType m = null;
		if (ISBN == null) {
			this.logger.debug("getBookByISBN: ISBN is null.");
		} else {
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaQuery<BookType> q = cb.createQuery(BookType.class);
			Root<BookType> c = q.from(BookType.class);
			q.where(cb.equal(c.<String> get(BookType_.ISBN), ISBN));
			TypedQuery<BookType> typedQuery = this.em.createQuery(q);
			try {
				m = typedQuery.getSingleResult();
				this.logger.debug("getBookByISBN: " + ToStringBuilder.reflectionToString(m));
			} catch (NoResultException e) {
				this.logger.debug("getBookByISBN: non value found for ISBN=" + ISBN);
				m = null;
			}
		}
		return m;
	}

	/**
	 * @param title
	 * @param max
	 * @return all {@link BookType}s with the title contained.
	 */
	@Transactional(value = PersistenceConfig.TRANSACTION_MANAGER_NAME, propagation = Propagation.REQUIRED)
	public List<BookType> getBookByTitle(final String title, final BigInteger max) {
		List<BookType> value = new ArrayList<>();
		Optional.of(title).ifPresent(t -> {
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaQuery<BookType> q = cb.createQuery(BookType.class);
			Root<BookType> c = q.from(BookType.class);
			q.where(cb.like(c.<String> get(BookType_.title), String.format("%%%s%%", title)));
			q.orderBy(cb.asc(c.<String> get(BookType_.title)));
			long maximum = Integer.MAX_VALUE;
			if (Objects.nonNull(max)) {
				maximum = max.longValue();
			}
			value.addAll(this.em.createQuery(q).getResultList().stream().limit(maximum).collect(Collectors.toList()));
		});
		return value;
	}

	/**
	 * @param value
	 *                  the {@link BookType} to save.
	 * @return the created {@link BookType}.
	 */
	@Transactional(value = PersistenceConfig.TRANSACTION_MANAGER_NAME, propagation = Propagation.REQUIRED)
	public BookType saveBook(final BookType value) {
		if (value != null) {
			try {
				this.em.merge(value);
				this.logger.debug("saveBook id=" + value.getUUID());
			} catch (RuntimeException e) {
				e.printStackTrace();
				throw e;
			}
			return value;
		} else {
			this.logger.debug("saveBook: value is null.");
			return null;
		}
	}

}
