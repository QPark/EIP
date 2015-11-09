package com.samples.platform.client.impl;

import javax.xml.bind.JAXBElement;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.samples.platform.client.LibraryServiceClient;
import com.samples.platform.model.library.BookCriteriaType;
import com.samples.platform.model.library.BookType;
import com.samples.platform.service.library.msg.CreateBookRequestType;
import com.samples.platform.service.library.msg.CreateBookResponseType;
import com.samples.platform.service.library.msg.GetBookRequestType;
import com.samples.platform.service.library.msg.GetBookResponseType;
import com.samples.platform.service.library.msg.ObjectFactory;

/**
 * Implementation of the {@link LibraryServiceClient}.
 *
 * @author bhausen
 */
public class LibraryServiceClientImpl extends WebServiceGatewaySupport
		implements LibraryServiceClient {
	/** Service {@link ObjectFactory}. */
	private final ObjectFactory ofService = new ObjectFactory();

	/**
	 * @see com.samples.platform.client.LibraryServiceClient#getBook(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public GetBookResponseType getBook(final String isbn,
			final String userName) {
		GetBookResponseType getBookResponse = this.ofService
				.createGetBookResponseType();
		if (isbn != null && isbn.trim().length() > 0) {
			GetBookRequestType request = this.ofService
					.createGetBookRequestType();
			request.setUserName(userName);
			request.setCriteria(new BookCriteriaType());
			request.getCriteria().setISBN(isbn);

			@SuppressWarnings("unchecked")
			JAXBElement<GetBookResponseType> response = (JAXBElement<GetBookResponseType>) this
					.getWebServiceTemplate().marshalSendAndReceive(
							this.ofService.createGetBookRequest(request));
			getBookResponse = response.getValue();
		}
		return getBookResponse;
	}

	/**
	 * @see com.samples.platform.client.LibraryServiceClient#createBook(com.samples.platform.model.library.BookType,
	 *      java.lang.String)
	 */
	@Override
	public CreateBookResponseType createBook(final String userName,
			final BookType... books) {
		CreateBookResponseType createBookResponse = this.ofService
				.createCreateBookResponseType();
		if (books != null && books.length > 0) {
			CreateBookRequestType request = this.ofService
					.createCreateBookRequestType();
			request.setUserName(userName);
			for (BookType book : books) {
				request.getBook().add(book);
			}

			@SuppressWarnings("unchecked")
			JAXBElement<CreateBookResponseType> response = (JAXBElement<CreateBookResponseType>) this
					.getWebServiceTemplate().marshalSendAndReceive(
							this.ofService.createCreateBookRequest(request));
			createBookResponse = response.getValue();
		}
		return createBookResponse;
	}

}
