/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.client;

import javax.xml.bind.JAXBElement;

import com.samples.platform.model.library.BookType;
import com.samples.platform.service.library.msg.CreateBookRequestType;
import com.samples.platform.service.library.msg.CreateBookResponseType;
import com.samples.platform.service.library.msg.ObjectFactory;

/**
 * Extension of the {@link LibraryServiceClient}.
 *
 * @author bhausen
 */
public class LibraryServiceClientExtension extends LibraryServiceClient {
	/**
	 * @return get the {@link CreateBookRequestType}
	 * @see ObjectFactory#createCreateBookRequestType()
	 */
	public CreateBookRequestType getCreateBookRequest() {
		return this.getObjectFactory().createCreateBookRequestType();
	}

	public CreateBookResponseType createBook(final String userName,
			final BookType... books) {
		CreateBookResponseType createBookResponse = this.getObjectFactory()
				.createCreateBookResponseType();
		if (books != null && books.length > 0) {
			CreateBookRequestType request = this.getObjectFactory()
					.createCreateBookRequestType();
			request.setUserName(userName);
			for (BookType book : books) {
				request.getBook().add(book);
			}

			@SuppressWarnings("unchecked")
			JAXBElement<CreateBookResponseType> response = (JAXBElement<CreateBookResponseType>) this
					.getWebServiceTemplate()
					.marshalSendAndReceive(this.getObjectFactory()
							.createCreateBookRequest(request));
			createBookResponse = response.getValue();
		}
		return createBookResponse;
	}

}
