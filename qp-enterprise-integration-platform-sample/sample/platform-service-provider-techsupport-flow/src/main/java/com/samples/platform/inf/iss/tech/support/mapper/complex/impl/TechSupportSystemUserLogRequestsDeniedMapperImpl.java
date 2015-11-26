package com.samples.platform.inf.iss.tech.support.mapper.complex.impl;

import org.springframework.stereotype.Component;

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
	 * @see com.samples.platform.inf.iss.tech.support.mapper.complex.TechSupportSystemUserLogRequestsDeniedMapper#createMappingType(com.samples.platform.inf.iss.tech.support.ext.type.ExtSystemUserLogType)
	 */
	@Override
	public TechSupportSystemUserLogRequestsDeniedMappingType createMappingType(
			final ExtSystemUserLogType systemUserReport) {
		TechSupportSystemUserLogRequestsDeniedMappingType mappingType = this.of
				.createTechSupportSystemUserLogRequestsDeniedMappingType();
		mappingType.setSystemUserReport(systemUserReport);

		long mappedReturnValue = systemUserReport.getRequestsDenied();
		mappingType.setValue(mappedReturnValue);
		mappingType.setReturn(mappedReturnValue);

		return mappingType;
	}
}
