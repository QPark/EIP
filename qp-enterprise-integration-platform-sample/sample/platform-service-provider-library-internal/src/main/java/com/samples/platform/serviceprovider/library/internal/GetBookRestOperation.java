package com.samples.platform.serviceprovider.library.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samples.platform.model.library.BookType;
import com.samples.platform.serviceprovider.library.internal.dao.PlatformDao;

/**
 * @author bhausen
 */
@RestController
public class GetBookRestOperation {
	/** The {@link PlatformDao}. */
	@Autowired
	private PlatformDao dao;

	/**
	 * @param userName
	 * @param id
	 * @param isbn
	 * @param model
	 * @return the list of {@link BookType}s.
	 */
	@RequestMapping(value = "/restservices/library/getBook",
			method = RequestMethod.GET)
	public List<BookType> getBook(
			@RequestParam("userName") final String userName,
			@RequestParam("id") final List<String> id,
			@RequestParam("ISBN") final String isbn, final Model model) {
		List<BookType> value = new ArrayList<>();
		if (Objects.nonNull(isbn) && isbn.trim().length() > 0) {
			value.add(this.dao.getBookByISBN(isbn));
		} else if (Objects.nonNull(id)) {
			id.stream().forEach(idx -> value.add(this.dao.getBookById(idx)));
		}
		return value;
	}
}
