/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.inf.iss.tech.support.mapper.complex.impl;

import org.springframework.stereotype.Component;

import com.qpark.eip.inf.FlowContext;
import com.samples.platform.inf.iss.tech.support.ext.type.ExtSystemUserLogType;
import com.samples.platform.inf.iss.tech.support.map.ObjectFactory;
import com.samples.platform.inf.iss.tech.support.map.TechSupportSystemUserLogRequestsDeniedMappingType;
import com.samples.platform.inf.iss.tech.support.mapper.complex.TechSupportSystemUserLogRequestsDeniedMapper;

/**
 * The {@link TechSupportSystemUserLogRequestsDeniedMapper} implementation.
 * <p/>
 * Number of denied requests in long.
 * <p/>
 * This is a ComplexMappingType.
 *
 * @author bhausen
 */
@Component
public class TechSupportSystemUserLogRequestsDeniedMapperImpl
		implements TechSupportSystemUserLogRequestsDeniedMapper {
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * Creates the {@link TechSupportSystemUserLogRequestsDeniedMappingType}
	 * defined as <i>TechSupport.SystemUserLog.requestsDeniedMappingType</i> in
	 * name space
	 * <i>http://www.samples.com/Interfaces/TechnicalSupportMappingTypes</i>.
	 * This name space is stored in file
	 * Interfaces/Mapping/TechnicalSupport/TechnicalSupportMappingTypes.xsd.
	 * 
	 * @see com.samples.platform.inf.iss.tech.support.mapper.complex.TechSupportSystemUserLogRequestsDeniedMapper#createMappingType(com.samples.platform.inf.iss.tech.support.ext.type.ExtSystemUserLogType,
	 *      com.qpark.eip.inf.FlowContext)
	 */
	@Override
	public TechSupportSystemUserLogRequestsDeniedMappingType createMappingType(
			final ExtSystemUserLogType systemUserReport,
			final FlowContext flowContext) {
		TechSupportSystemUserLogRequestsDeniedMappingType mappingType = this.of
				.createTechSupportSystemUserLogRequestsDeniedMappingType();
		mappingType.setSystemUserReport(systemUserReport);

		long mappedReturnValue = systemUserReport.getRequestsDenied();
		mappingType.setValue(mappedReturnValue);
		mappingType.setReturn(mappedReturnValue);

		return mappingType;
	}
}
