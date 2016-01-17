package com.qpark.eip.core.model.analysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author bhausen
 */
public class PersistModelAnalysis {
	@Autowired
	private ModelAnalysisDao modelAnalysisDao;
	/** The enterprise name of the model. */
	@Value("${model.enterpriseName}")
	private String enterpriseName;
	/** The base package name of the model. */
	@Value("${model.basePackageName}")
	private String basePackageName;
	/** The path to the enterprise model. */
	@Value("${model.path}")
	private String modelPath;

	public void persist() {
		Analysis a = new AnalysisProvider().createEnterprise(
				this.enterpriseName, this.basePackageName, this.modelPath);
		this.modelAnalysisDao.saveEnterprise(a.getEnterprise());
	}
}
