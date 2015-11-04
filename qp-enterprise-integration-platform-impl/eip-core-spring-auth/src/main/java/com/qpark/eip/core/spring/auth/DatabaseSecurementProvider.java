package com.qpark.eip.core.spring.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;

import com.qpark.eip.core.spring.security.DefaultEipSecurmentPropertyProvider;

/**
 * Reads user credential to call other services out of the database.
 * 
 * @author bhausen
 */
public class DatabaseSecurementProvider
		extends DefaultEipSecurmentPropertyProvider {
	/** The {@link DatabaseUserProvider}. */
	@Autowired
	private DatabaseUserProvider databaseUserProvider;

	/**
	 * @see com.qpark.eip.core.spring.security.DefaultEipSecurmentPropertyProvider#setSecurementPassword(java.lang.String)
	 */
	@Override
	public void setSecurementPassword(final String securementPassword) {
		throw new IllegalArgumentException(
				"In DatabaseSecurementProvider the password is read out of the database.");
	}

	/**
	 * @see com.qpark.eip.core.spring.security.DefaultEipSecurmentPropertyProvider#getSecurementPassword()
	 */
	@Override
	public String getSecurementPassword() {
		User user = this.databaseUserProvider
				.getUser(this.getSecurementUsername());
		return user.getPassword();
	}
}
