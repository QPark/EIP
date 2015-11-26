package com.qpark.eip.core.spring.statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.domain.persistencedefinition.FlowLogMessageType;
import com.qpark.eip.core.persistence.AsyncDatabaseOperation;
import com.qpark.eip.core.persistence.AsyncDatabaseOperationPoolProvider;
import com.qpark.eip.core.spring.statistics.dao.StatisticsLoggingDao;

/**
 * @author bhausen
 */
public class AsyncFlowLogMessagePersistence {
	/** Enumeration FlowLogMessage Classification Data Incompatibility */
	public static final String CLASSIFICATION_DATA_INCOMPATIBILITY = "0a5c2391-e205-3d9b-0016-000000000003";
	/** Enumeration FlowLogMessage Classification Data Inconsistency */
	public static final String CLASSIFICATION_DATA_INCONSISTENCY = "0a5c2391-e205-3d9b-0016-000000000001";
	/** Enumeration FlowLogMessage Classification Transfer Receipt */
	public static final String CLASSIFICATION_TRANSFER_RECEIPT = "0a5c2391-e205-3d9b-0016-000000000004";
	/**
	 * Enumeration FlowLogMessage Classification Value not provided - defaulted
	 * or null
	 */
	public static final String CLASSIFICATION_VALUE_NOT_PROVIDED_DEFAULTED_OR_NULL = "0a5c2391-e205-3d9b-0016-000000000002";
	/** Enumeration FlowLogMessage Severity Error */
	public static final String SEVERITY_ERROR = "0a5c2391-e205-3d9b-0014-000000000003";
	/** Enumeration FlowLogMessage Severity Information */
	public static final String SEVERITY_INFORMATION = "0a5c2391-e205-3d9b-0014-000000000001";
	/** Enumeration FlowLogMessage Severity Warning */
	public static final String SEVERITY_WARNING = "0a5c2391-e205-3d9b-0014-000000000002";
	/** Enumeration FlowLogMessage Step Request execution */
	public static final String STEP_REQUEST_EXECUTION = "0a5c2391-e205-3d9b-0015-000000000001";
	/** Enumeration FlowLogMessage Step Request mapping */
	public static final String STEP_REQUEST_MAPPING = "0a5c2391-e205-3d9b-0015-000000000003";
	/** Enumeration FlowLogMessage Step Response mapping */
	public static final String STEP_RESPONSE_MAPPING = "0a5c2391-e205-3d9b-0015-000000000004";
	/** Enumeration FlowLogMessage Step Entity mapping */
	public static final String STEP_ENTITY_MAPPING = "0a5c2391-e205-3d9b-0015-000000000005";
	/** Enumeration FlowLogMessage Step Attribute mapping */
	public static final String STEP_ATTRIBUTE_MAPPING = "0a5c2391-e205-3d9b-0015-000000000006";
	/** Enumeration FlowLogMessage Step Response processing */
	public static final String STEP_RESPONSE_PROCESSING = "0a5c2391-e205-3d9b-0015-000000000002";
	/** Enumeration FlowLogMessage Type Attribute */
	public static final String TYPE_ATTRIBUTE = "0a5c2391-e205-3d9b-0013-000000000004";
	/** Enumeration FlowLogMessage Type Entity */
	public static final String TYPE_ENTITY = "0a5c2391-e205-3d9b-0013-000000000003";
	/** Enumeration FlowLogMessage Type Flow */
	public static final String TYPE_FLOW = "0a5c2391-e205-3d9b-0013-000000000001";
	/** Enumeration FlowLogMessage Type Mapping */
	public static final String TYPE_MAPPING = "0a5c2391-e205-3d9b-0013-000000000002";

	/** The {@link AsyncDatabaseOperationPoolProvider}. */
	@Autowired
	private AsyncDatabaseOperationPoolProvider pool;
	/** The {@link BusUtilDao}. */
	@Autowired
	private StatisticsLoggingDao dao;

	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory.getLogger(AsyncFlowLogMessagePersistence.class);

	/**
	 * Submit the {@link FlowLogMessageType} to be stored in the database.
	 * 
	 * @param log
	 *            the {@link FlowLogMessageType}
	 */
	public void submitApplicationUserLogType(final FlowLogMessageType log) {
		if (log != null) {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("{} {}", log.getFlowName(), log.getDataDescription() != null
						? log.getDataDescription().replaceAll("\\\\n", " ").replaceAll("\\\\t", " ") : "");
			}
			this.pool.submit(new AsyncDatabaseOperation(this.dao, "No userName supplied", log));
		}
	}
}
