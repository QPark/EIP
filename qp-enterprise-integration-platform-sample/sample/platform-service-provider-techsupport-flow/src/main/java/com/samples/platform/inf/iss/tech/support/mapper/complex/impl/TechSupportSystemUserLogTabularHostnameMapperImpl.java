package com.samples.platform.inf.iss.tech.support.mapper.complex.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.qpark.eip.inf.FlowContext;
import com.samples.platform.inf.iss.tech.support.map.ObjectFactory;
import com.samples.platform.inf.iss.tech.support.mapper.complex.TechSupportSystemUserLogTabularHostnameMapper;

/**
 * The {@link TechSupportSystemUserLogTabularHostnameMapper} implementation.
 * <p/>
 * Map the fixed input string to the output string.
 * <p/>
 * This is a ComplexMappingType.
 */
@Component
public class TechSupportSystemUserLogTabularHostnameMapperImpl
		implements TechSupportSystemUserLogTabularHostnameMapper {
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/** The {@link Map} to support tabular mappings. */
	private final Map<String, String> tabularValueMap = new HashMap<String, String>();

	public TechSupportSystemUserLogTabularHostnameMapperImpl() {
		this.tabularValueMap.put(DEFAULT_IN0, DEFAULT_OUT0);
		this.tabularValueMap.put(DEFAULT_IN1, DEFAULT_OUT1);
		this.tabularValueMap.put(DEFAULT_IN2, DEFAULT_OUT2);
	}

	/**
	 * Creates the
	 * {@link com.samples.platform.inf.iss.tech.support.map.TechSupportSystemUserLogTabularHostnameMappingType}
	 * defined as <i>TechSupport.SystemUserLog.tabularHostnameMappingType</i> in
	 * name space
	 * <i>http://www.samples.com/Interfaces/TechnicalSupportMappingTypes</i>.
	 * This name space is stored in file
	 * Interfaces/Mapping/TechnicalSupport/TechnicalSupportMappingTypes.xsd.
	 *
	 * @see com.samples.platform.inf.iss.tech.support.mapper.complex.TechSupportSystemUserLogTabularHostnameMapper#createMappingType(com.samples.platform.inf.iss.tech.support.map.ApplicationUserLogHostNameMappingType,
	 *      com.qpark.eip.inf.FlowContext)
	 */
	@Override
	public com.samples.platform.inf.iss.tech.support.map.TechSupportSystemUserLogTabularHostnameMappingType createMappingType(
			final com.samples.platform.inf.iss.tech.support.map.ApplicationUserLogHostNameMappingType systemUserReport,
			final FlowContext flowContext) {
		com.samples.platform.inf.iss.tech.support.map.TechSupportSystemUserLogTabularHostnameMappingType mappingType = this.of
				.createTechSupportSystemUserLogTabularHostnameMappingType();
		mappingType.setSystemUserReport(systemUserReport);

		Object mappedValue = this.tabularValueMap
				.get(systemUserReport.getReturn());
		if (mappedValue == null) {
			for (String key : this.tabularValueMap.keySet()) {
				if (systemUserReport.getReturn().matches(key)) {
					mappedValue = this.tabularValueMap.get(key);
				}
			}
		}
		if (mappedValue == null) {
			mappedValue = DEFAULT_DEFAULT;
		}
		mappingType.setValue(mappedValue);
		mappingType.setReturn(String.valueOf(mappedValue));
		return mappingType;
	}
}
