package com.qpark.eip.core.sftp;

import java.util.List;
import java.util.Optional;

/**
 * Create the types T.
 *
 * @author bhausen
 */
public interface TypeFactory<T> {
	/**
	 * @param bytes
	 *            the byte array.
	 * @return get the list of Ts form the byte array.
	 * @throws Exception
	 */
	List<T> create(Optional<byte[]> bytes) throws Exception;
}
