package com.samples.platform.service.common;

import java.util.Optional;

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
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(HeardBeatController.class);
	/** The {@link OperationProviderCommon} */
	@Autowired
	private OperationProviderCommon common;

	/** Check common.GetServiceStatus. */
	@Scheduled(fixedDelay = 30000)
	public void check() {
		GetServiceStatusRequestType request = new GetServiceStatusRequestType();
		this.logger.info(Optional
				.ofNullable(this.common.getServiceStatus(request))
				.map(r -> r.getStatus()).orElse("Serive does not answer"));
	}
}
