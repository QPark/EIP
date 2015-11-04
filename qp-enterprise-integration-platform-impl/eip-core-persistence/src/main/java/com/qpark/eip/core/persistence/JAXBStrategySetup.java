/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.�r.l. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License
 * v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.qpark.eip.core.persistence;

import org.jvnet.jaxb2_commons.lang.DefaultEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;

public class JAXBStrategySetup {
	public static void setup() {
		/*
		 * Be sure to have the right equals and hashCode strategy loaded jvm
		 * wide. Deprecated this is already overwritten in
		 * postProcessBeforeInitialization
		 */
		DefaultEqualsStrategy.INSTANCE = ExtendedJAXBEqualsStrategy.INSTANCE;
		JAXBEqualsStrategy.INSTANCE = ExtendedJAXBEqualsStrategy.INSTANCE;
		/* DefaultHashCodeStrategy.INSTANCE is final! */
		/*
		 * DefaultHashCodeStrategy.INSTANCE =
		 * ExtendedJAXBHashCodeStrategy.INSTANCE;
		 */
		JAXBHashCodeStrategy.INSTANCE = ExtendedJAXBHashCodeStrategy.INSTANCE;
	}
}
