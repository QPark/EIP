/*******************************************************************************
 * Copyright (c) 2017 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.poi.test;

import java.util.ArrayList;
import java.util.List;

/**
 * A person.
 *
 * @author bhausen
 */
public class Person {
	/** A list of {@link Person}s. */
	private static final List<Person> persons = new ArrayList<>();

	/**
	 * @return the list of {@link Person}s.
	 */
	public static List<Person> getPersons() {
		if (persons.isEmpty()) {
			Person p = new Person();
			persons.add(p);
			p.setAge(10);
			p.setFemale(true);
			p.setFirstName("Anne");
			p.setLastName("Smith");
			p.setStreet("Rheinweg");
			p.setHouseNumber(23);
			p.setCity("Colone");
			p.setZipCode("D-12387");
			p.setCountry("Germany");

			p = new Person();
			persons.add(p);
			p.setAge(11);
			p.setFemale(false);
			p.setFirstName("Chris");
			p.setLastName("Doe");
			p.setStreet("Seineloin");
			p.setHouseNumber(42);
			p.setZipCode("F-1");
			p.setCity("Paris");
			p.setCountry("France");

			p = new Person();
			persons.add(p);
			p.setAge(12);
			p.setFemale(true);
			p.setFirstName("Eve");
			p.setLastName("Adams");
			p.setStreet("Via d'tiber");
			p.setHouseNumber(1);
			p.setZipCode("I-12387");
			p.setCity("Rome");
			p.setCountry("Italy");
		}
		return persons;
	}

	/** The age. */
	private int age;
	/** The city. */
	private String city;
	/** The country. */
	private String country;
	/** Female or male. */
	private boolean female;
	/** The first name. */
	private String firstName;
	/** The last name. */
	private String lastName;
	/** The street name. */
	private String street;
	/** The house number. */
	private int houseNumber;
	/** The zip code. */
	private String zipCode;

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return this.zipCode;
	}

	/**
	 * @param zipCode
	 *            the zipCode to set
	 */
	public void setZipCode(final String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @return the houseNumber
	 */
	public int getHouseNumber() {
		return this.houseNumber;
	}

	/**
	 * @param houseNumber
	 *            the houseNumber to set
	 */
	public void setHouseNumber(final int houseNumber) {
		this.houseNumber = houseNumber;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return this.age;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return this.street;
	}

	/**
	 * @return the female
	 */
	public boolean isFemale() {
		return this.female;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(final int age) {
		this.age = age;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(final String city) {
		this.city = city;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(final String country) {
		this.country = country;
	}

	/**
	 * @param female
	 *            the female to set
	 */
	public void setFemale(final boolean female) {
		this.female = female;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(final String street) {
		this.street = street;
	}
}
