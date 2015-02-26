/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Bernhard Hausen - Initial API and implementation
 *
 ******************************************************************************/
package com.qpark.eip.core;

import java.util.TreeMap;

public class BestGuessMapper extends PropertyDescriptorUtil {
	/** The {@link Logger}. */
	private org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(BestGuessMapper.class);

	/**
	 * Maps all matching properties from a to b.
	 * @param a
	 * @param b
	 */
	public void map(final Object a, final Object b,
			final boolean checkSimpleClassname) {
		TreeMap<String, ObjectProperties> aOps = getObjectProperties(a);
		TreeMap<String, ObjectProperties> bOps = getObjectProperties(b);
		Object aObject;
		Object bObject;
		ObjectProperties bOp;
		for (ObjectProperties aOp : aOps.values()) {
			aObject = null;
			bObject = null;
			if (!aOp.name.equalsIgnoreCase("HJID")) {
				try {
					bOp = bOps.get(aOp.name);
					if (bOp != null
							&& (!checkSimpleClassname || checkSimpleClassname
									&& bOp.type.getSimpleName().equals(
											aOp.type.getSimpleName()))
							&& bOp.write != null) {
						aObject = aOp.read.invoke(a,
								PropertyDescriptorUtil.EMPTY);
						if (aObject != null
								&& aOp.type.getPackage().getName()
										.startsWith("com.ses.")) {
							bObject = bOp.read.invoke(b,
									PropertyDescriptorUtil.EMPTY);
							if (bObject == null) {
								bObject = getObject(bOp.type);
							}
							this.map(aObject, bObject, checkSimpleClassname);
						}
						bOp.write.invoke(b, new Object[] { aObject });
					}
				} catch (Exception e) {
					this.logger.debug("Could not map property {}.{}", a
							.getClass().getName(), aOp.name);
					e.printStackTrace();
				}
			}
		}
	}

	private static final Class[] CTOR_EMPTY = new Class[0];

}
