/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.serviceprovider.library.internal.dao;

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

/**
 * @author bhausen
 */
@Repository
public class PlatformDao {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(PlatformDao.class);

	/** The {@link EntityManager}. */
	@PersistenceContext(unitName = "com.samples.platform.domain",
			name = "ComSamplesPlatformEntityManagerFactory")
	private EntityManager em;

	/**
	 * @param value
	 *            the {@link BookType} to create.
	 * @return the created {@link BookType}.
	 */
	@Transactional(value = "ComSamplesPlatformTransactionManager",
			propagation = Propagation.REQUIRED)
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
	 *            the id of the book to delete.
	 */
	@Transactional(value = "ComSamplesPlatformTransactionManager",
			propagation = Propagation.REQUIRED)
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
	 * @param ISBN
	 *            the ISBN of the book to find.
	 * @return the {@link BookType}.
	 */
	@Transactional(value = "ComSamplesPlatformTransactionManager",
			propagation = Propagation.REQUIRED)
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
				this.logger.debug("getBookByISBN: "
						+ ToStringBuilder.reflectionToString(m));
			} catch (NoResultException e) {
				this.logger.debug(
						"getBookByISBN: non value found for ISBN=" + ISBN);
				m = null;
			}
		}
		return m;
	}

	/**
	 * @param uuid
	 *            the id of the book to find.
	 * @return the {@link BookType}.
	 */
	@Transactional(value = "ComSamplesPlatformTransactionManager",
			propagation = Propagation.REQUIRED)
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
				this.logger.debug("getBookByISBN: "
						+ ToStringBuilder.reflectionToString(m));
			} catch (NoResultException e) {
				this.logger
						.debug("getBookByISBN: non value found for id=" + uuid);
				m = null;
			}
		}
		return m;
	}

	/**
	 * @param value
	 *            the {@link BookType} to save.
	 * @return the created {@link BookType}.
	 */
	@Transactional(value = "ComSamplesPlatformTransactionManager",
			propagation = Propagation.REQUIRED)
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
