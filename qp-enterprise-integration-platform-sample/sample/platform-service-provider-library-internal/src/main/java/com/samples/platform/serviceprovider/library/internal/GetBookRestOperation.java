package com.samples.platform.serviceprovider.library.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	@RequestMapping(value = "/library/getBook", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<BookType> getBook(
			@RequestParam(value = "userName",
					required = true) final String userName,
			@RequestParam(value = "id", required = false) final List<String> id,
			@RequestParam(value = "ISBN", required = false) final String isbn,
			final Model model) {
		List<BookType> value = new ArrayList<>();
		if (Objects.nonNull(isbn) && isbn.trim().length() > 0) {
			value.add(this.dao.getBookByISBN(isbn));
		} else if (Objects.nonNull(id)) {
			id.stream().forEach(idx -> value.add(this.dao.getBookById(idx)));
		}
		BookType bt = new BookType();
		bt.setCategory("Roman");
		bt.setISBN("978-3548234106");
		bt.setLanguage("English");
		bt.setPrice(BigDecimal.valueOf(9.99));
		bt.setTitle("1984");
		bt.setUUID(UUID.nameUUIDFromBytes(bt.getTitle().getBytes()).toString());
		value.add(bt);
		return value;
	}
}
