package com.samples.platform.client;

import com.samples.platform.model.library.BookType;
import com.samples.platform.service.library.msg.CreateBookResponseType;
import com.samples.platform.service.library.msg.GetBookResponseType;
import com.springsource.insight.annotation.InsightOperation;

/**
 * Client to call the library service operations of the bus.
 *
 * @author bhausen
 */
public interface LibraryServiceClient {
	/**
	 * Get the book requested by the ISBN.
	 *
	 * @param isbn
	 *            the ISBN to look for.
	 * @param userName
	 *            the users name.
	 * @return the {@link GetBookResponseType}.
	 */
	@InsightOperation
	GetBookResponseType getBook(String isbn, String userName);

	/**
	 * Create the {@link BookType}.
	 *
	 * @param books
	 *            the {@link BookType} to create.
	 * @param userName
	 *            the users name.
	 * @return the {@link CreateBookResponseType}.
	 */
	@InsightOperation
	CreateBookResponseType createBook(String userName, BookType... books);
}
