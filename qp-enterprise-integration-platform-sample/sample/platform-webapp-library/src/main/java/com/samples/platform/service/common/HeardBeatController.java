package com.samples.platform.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.qpark.eip.service.common.msg.GetServiceStatusRequestType;
import com.samples.domain.serviceprovider.OperationProviderCommon;

/**
 * @author bhausen
 */
@Component
public class HeardBeatController {
	@Autowired
	private OperationProviderCommon common;

	@Scheduled(fixedDelay = 30000)
	public void push() {
		GetServiceStatusRequestType request = new GetServiceStatusRequestType();
		this.common.getServiceStatus(request);
	}
}
