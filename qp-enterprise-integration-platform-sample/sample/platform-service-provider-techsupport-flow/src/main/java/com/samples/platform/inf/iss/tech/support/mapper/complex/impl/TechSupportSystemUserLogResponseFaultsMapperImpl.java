package com.samples.platform.inf.iss.tech.support.mapper.complex.impl;

import org.springframework.stereotype.Component;

import com.samples.platform.inf.iss.tech.support.ext.type.ExtSystemUserLogType;
import com.samples.platform.inf.iss.tech.support.map.ObjectFactory;
import com.samples.platform.inf.iss.tech.support.map.TechSupportSystemUserLogResponseFaultsMappingType;
import com.samples.platform.inf.iss.tech.support.mapper.complex.TechSupportSystemUserLogResponseFaultsMapper;

/**
 * The {@link TechSupportSystemUserLogResponseFaultsMapper} implementation.
 * <p/>
 * Number of responses with faults in long.
 * <p/>
 * This is a ComplexMappingType.
 *
 * @author bhausen
 */
@Component
public class TechSupportSystemUserLogResponseFaultsMapperImpl
		implements TechSupportSystemUserLogResponseFaultsMapper {
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * Creates the {@link TechSupportSystemUserLogResponseFaultsMappingType}
	 * defined as <i>TechSupport.SystemUserLog.responseFaultsMappingType</i> in
	 * name space
	 * <i>http://www.samples.com/Interfaces/TechnicalSupportMappingTypes</i>.
	 * This name space is stored in file
	 * Interfaces/Mapping/TechnicalSupport/TechnicalSupportMappingTypes.xsd.
	 *
	 * @see com.samples.platform.inf.iss.tech.support.mapper.complex.TechSupportSystemUserLogResponseFaultsMapper#createMappingType(com.samples.platform.inf.iss.tech.support.ext.type.ExtSystemUserLogType)
	 */
	@Override
	public TechSupportSystemUserLogResponseFaultsMappingType createMappingType(
			final ExtSystemUserLogType systemUserReport) {
		TechSupportSystemUserLogResponseFaultsMappingType mappingType = this.of
				.createTechSupportSystemUserLogResponseFaultsMappingType();
		mappingType.setSystemUserReport(systemUserReport);

		long mappedReturnValue = systemUserReport.getResponseFaults();
		mappingType.setValue(mappedReturnValue);
		mappingType.setReturn(mappedReturnValue);
		return mappingType;
	}
}
