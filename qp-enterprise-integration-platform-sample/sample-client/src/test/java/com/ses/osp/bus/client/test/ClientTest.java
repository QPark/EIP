package com.ses.osp.bus.client.test;

import java.math.BigDecimal;
import java.util.TimeZone;

import org.apache.ws.security.util.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.samples.platform.client.LibraryServiceClientExtension;
import com.samples.platform.client.config.ClientConfig;
import com.samples.platform.model.library.BookCriteriaType;
import com.samples.platform.model.library.BookType;
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
		LibraryServiceClientExtension libraryClient = this.applicationContext
				.getBean(LibraryServiceClientExtension.class);

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
			}
		} else {
			this.logger.error(" run create book return null response.");
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
				this.logger.info(" run get book returned {} for ISBN {}",
						bt.getTitle(), ISBN);
			}
		} else {
			this.logger.error(" run get book return null response.");
		}

		this.logger.info("-run");
	}
}
