/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.service.common;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.qpark.eip.service.common.msg.GetServiceStatusRequestType;
import com.qpark.eip.service.domain.doc.msg.GetServiceIdRequestType;
import com.samples.domain.serviceprovider.OperationProviderCommon;
import com.samples.domain.serviceprovider.OperationProviderDomainDoc;
import com.samples.domain.serviceprovider.OperationProviderIssTechSupport;
import com.samples.domain.serviceprovider.OperationProviderLibrary;
import com.samples.platform.model.iss.tech.support.OperationEventType;
import com.samples.platform.model.library.BookCriteriaType;
import com.samples.platform.model.library.BookType;
import com.samples.platform.service.iss.tech.support.lockedoperation.AsyncLongRunningLockedOperation;
import com.samples.platform.service.iss.tech.support.lockedoperation.SyncShortRunningLockedOperation;
import com.samples.platform.service.iss.tech.support.msg.AppOperationEventRequestType;
import com.samples.platform.service.library.msg.CreateBookRequestType;
import com.samples.platform.service.library.msg.GetBookRequestType;
import com.samples.platform.serviceprovider.domaindoc.CreateServiceReport;

/**
 * @author bhausen
 */
@Component
public class HeardBeatController {
	/** The book title. */
	private static final String HITCHHIKER = "The Hitchhiker's Guide to the Galaxy";
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(HeardBeatController.class);
	/** The {@link OperationProviderCommon} */
	@Autowired
	private OperationProviderCommon common;
	/** The {@link OperationProviderDomainDoc} */
	@Autowired
	private OperationProviderDomainDoc domainDoc;
	/** The {@link OperationProviderIssTechSupport} */
	@Autowired
	private OperationProviderIssTechSupport issTechSupport;
	/** The {@link OperationProviderLibrary} */
	@Autowired
	private OperationProviderLibrary library;
	/** Volume count. */
	private int volume = 0;
	/** The {@link Random}. */
	private Random random = new Random(42);

	/** Check common.GetServiceStatus. */
	@Scheduled(fixedDelay = 30000)
	public void check() {
		GetServiceStatusRequestType requestGetServiceStatus = new GetServiceStatusRequestType();
		this.logger.info(Optional.ofNullable(this.common.getServiceStatus(requestGetServiceStatus))
				.map(r -> r.getStatus()).orElse("Service common::GetServiceStatus does not answer"));

		AppOperationEventRequestType requestAppOperation = new AppOperationEventRequestType();
		requestAppOperation.setUserName("bhausen");
		requestAppOperation.setOperationEvent(new OperationEventType());
		requestAppOperation.getOperationEvent().setEvent("Start");
		requestAppOperation.getOperationEvent().setOperationUUID(AsyncLongRunningLockedOperation.OPERATION_UUID);
		this.issTechSupport.appOperationEvent(requestAppOperation);

		requestAppOperation.getOperationEvent().setOperationUUID(SyncShortRunningLockedOperation.OPERATION_UUID);
		this.issTechSupport.appOperationEvent(requestAppOperation);

		requestAppOperation.getOperationEvent().setOperationUUID(CreateServiceReport.OPERATION_UUID);
		this.issTechSupport.appOperationEvent(requestAppOperation);

		GetServiceIdRequestType requestGetServiceId = new GetServiceIdRequestType();
		requestGetServiceId.setUserName("bhausen");
		this.logger.info(Optional.ofNullable(this.domainDoc.getServiceId(requestGetServiceId))
				.map(r -> r.getServiceId().stream().collect(Collectors.joining(", ")))
				.orElse("Service domain.doc::GetServiceId does not answer"));

		CreateBookRequestType requestCreateBook = new CreateBookRequestType();
		requestCreateBook.setUserName("bhausen");

		BookType book = new BookType();
		book.setCategory("Fantasy");
		book.setLanguage("English");
		book.setTitle(String.format("%s - Volume %d", HITCHHIKER, ++this.volume));
		book.setUUID(UUID.randomUUID().toString());
		book.setISBN(String.format("978-3-%05d-%03d-%d", Math.abs(this.random.nextInt() % 100000),
				Math.abs(this.random.nextInt() % 1000), Math.abs(this.random.nextInt() % 10)));
		requestCreateBook.getBook().add(book);
		this.logger.info(Optional.ofNullable(this.library.createBook(requestCreateBook))
				.map(r -> String.valueOf(r.getBook().size())).orElse("Service library::CreateBook does not answer"));

		GetBookRequestType requestGetBook = new GetBookRequestType();
		requestGetBook.setUserName("bhausen");
		requestGetBook.setCriteria(new BookCriteriaType());
		requestGetBook.getCriteria().setTitle(HITCHHIKER);
		requestGetBook.getCriteria().setMaxElements(BigInteger.valueOf(5L));
		this.logger.info(Optional.ofNullable(this.library.getBook(requestGetBook))
				.map(r -> r.getBook().stream().map(b -> b.getTitle()).collect(Collectors.joining(", ")))
				.orElse("Service library::GetBook does not answer"));

	}
}
