package com.samples.platform.inf.iss.tech.support.mapper.complex.impl;

import org.springframework.stereotype.Component;

import com.samples.platform.inf.iss.tech.support.ext.type.ExtSystemUserLogType;
import com.samples.platform.inf.iss.tech.support.map.ObjectFactory;
import com.samples.platform.inf.iss.tech.support.map.TechSupportSystemUserLogRequestsGrantedMappingType;
import com.samples.platform.inf.iss.tech.support.mapper.complex.TechSupportSystemUserLogRequestsGrantedMapper;

/**
 * The {@link TechSupportSystemUserLogRequestsGrantedMapper} implementation.
 * <p/>
 * Number of requests in long.
 * <p/>
 * This is a ComplexMappingType.
 *
 * @author bhausen
 */
@Component
public class TechSupportSystemUserLogRequestsGrantedMapperImpl
		implements TechSupportSystemUserLogRequestsGrantedMapper {
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * Creates the {@link TechSupportSystemUserLogRequestsGrantedMappingType}
	 * defined as <i>TechSupport.SystemUserLog.requestsGrantedMappingType</i> in
	 * name space
	 * <i>http://www.samples.com/Interfaces/TechnicalSupportMappingTypes</i>.
	 * This name space is stored in file
	 * Interfaces/Mapping/TechnicalSupport/TechnicalSupportMappingTypes.xsd.
	 *
	 * @see com.samples.platform.inf.iss.tech.support.mapper.complex.TechSupportSystemUserLogRequestsGrantedMapper#createMappingType(com.samples.platform.inf.iss.tech.support.ext.type.ExtSystemUserLogType)
	 */
	@Override
	public TechSupportSystemUserLogRequestsGrantedMappingType createMappingType(
			final ExtSystemUserLogType systemUserReport) {
		TechSupportSystemUserLogRequestsGrantedMappingType mappingType = this.of
				.createTechSupportSystemUserLogRequestsGrantedMappingType();
		mappingType.setSystemUserReport(systemUserReport);

		long mappedReturnValue = systemUserReport.getRequestsGranted();
		mappingType.setValue(mappedReturnValue);
		mappingType.setReturn(mappedReturnValue);

		return mappingType;
	}
}
