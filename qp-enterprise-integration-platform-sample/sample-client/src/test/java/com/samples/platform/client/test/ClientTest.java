/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.client.test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.TimeZone;

import org.apache.ws.security.util.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.samples.platform.client.IssTechSupportServiceClientExtension;
import com.samples.platform.client.LibraryServiceClientExtension;
import com.samples.platform.client.config.ClientConfig;
import com.samples.platform.model.iss.tech.support.SystemUserReportType;
import com.samples.platform.model.library.BookCriteriaType;
import com.samples.platform.model.library.BookType;
import com.samples.platform.service.iss.tech.support.msg.GetAggregatedReferenceDataResponseType;
import com.samples.platform.service.iss.tech.support.msg.GetForwardedReferenceDataResponseType;
import com.samples.platform.service.iss.tech.support.msg.GetSystemUserReportResponseType;
import com.samples.platform.service.library.msg.CreateBookResponseType;
import com.samples.platform.service.library.msg.GetBookRequestType;
import com.samples.platform.service.library.msg.GetBookResponseType;

/**
 * The client API.
 *
 * @author bhausen
 */
public class ClientTest {
	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory.getLogger(ClientTest.class);

	/**
	 * Start test of the client.
	 *
	 * @param args
	 *            no args expected.
	 */
	public static void main(final String[] args) {
		try {
			new ClientTest().run();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	/** Create the client test. */
	public ClientTest() {
		this.applicationContext = new AnnotationConfigApplicationContext(
				ClientConfig.class);
		this.logger.info("Default timezone is {}",
				TimeZone.getDefault().getID());
	}

	/** The client tests {@link ApplicationContext}. */
	private final ApplicationContext applicationContext;

	/** Run the test. */
	private void run() {
		this.logger.info("+run");
		this.logger.info(" run Get client bean");
		try {
			ObjectMapper json = new ObjectMapper();
			json.enable(SerializationFeature.INDENT_OUTPUT);

			LibraryServiceClientExtension libraryClient = this.applicationContext
					.getBean(LibraryServiceClientExtension.class);
			IssTechSupportServiceClientExtension issTechSupportClient = this.applicationContext
					.getBean(IssTechSupportServiceClientExtension.class);

			String userName = "userName";

			String ISBN = UUIDGenerator.getUUID();

			this.logger.info(" run Call bus to create a book");
			BookType book = new BookType();
			book.setISBN(ISBN);
			book.setCategory("Category");
			book.setLanguage("Language");
			book.setPrice(BigDecimal.TEN);
			book.setTitle("Title " + ISBN);
			book.setUUID(UUIDGenerator.getUUID());
			CreateBookResponseType createBookResponse = libraryClient
					.createBook(userName, book);
			if (createBookResponse != null) {
				for (BookType bt : createBookResponse.getBook()) {
					this.logger.info(" run created book ISBN=", bt.getISBN());
					this.logger.info(" run Book: {}",
							json.writeValueAsString(bt));
				}
			} else {
				this.logger.error(" run create book returned null response.");
			}
			this.logger.info(" run Call bus to get the created book");
			BookCriteriaType criteria = new BookCriteriaType();
			criteria.setISBN(ISBN);
			GetBookRequestType getBookRequest = libraryClient
					.createGetBookRequestType();
			getBookRequest.setCriteria(criteria);
			GetBookResponseType getBookResponse = libraryClient
					.getBook(getBookRequest);
			if (getBookResponse != null) {
				for (BookType bt : getBookResponse.getBook()) {
					this.logger.info(" run got book for ISBN {}: {}", ISBN,
							json.writeValueAsString(bt));
				}
			} else {
				this.logger.error(" run get book returned null response.");
			}

			GetAggregatedReferenceDataResponseType aggregatedReferenceData = issTechSupportClient
					.getAggregatedReferenceData(userName);
			if (aggregatedReferenceData != null) {
				if (aggregatedReferenceData.getReferenceData().size() == 2) {
					this.logger.info(
							" run got aggregated ReferenceData list size 2.");
				} else {
					this.logger.error(
							" run get aggregated reference data returned list size of {}!",
							aggregatedReferenceData.getReferenceData().size());
				}
			} else {
				this.logger.error(
						" run get aggregated reference data returned null response.");
			}

			GetForwardedReferenceDataResponseType forwardedReferenceData = issTechSupportClient
					.getForwardedReferenceData(userName);
			if (forwardedReferenceData != null) {
				if (forwardedReferenceData.getReferenceData().size() == 1) {
					this.logger.info(
							" run got forwarded ReferenceData list size 1.");
				} else {
					this.logger.error(
							" run get forwarded reference data returned list size of {}!",
							forwardedReferenceData.getReferenceData().size());
				}
			} else {
				this.logger.error(
						" run get forwarded reference data returned null response.");
			}

			GetSystemUserReportResponseType systemUserReport = issTechSupportClient
					.getSystemUserReport(userName, new Date());
			if (systemUserReport != null) {
				for (SystemUserReportType report : systemUserReport
						.getReport()) {
					this.logger.info(" run system user report: {}",
							json.writeValueAsString(report));
				}
			} else {
				this.logger.error(
						" run get system user report returned null response.");
			}

		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
		}
		this.logger.info("-run");
	}
}
