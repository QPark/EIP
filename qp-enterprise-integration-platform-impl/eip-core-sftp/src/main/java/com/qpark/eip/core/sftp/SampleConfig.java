package com.qpark.eip.core.sftp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;

/**
 * Example how to configure the {@link SftpGateway}.
 *
 * @author bhausen
 */
@Configuration
@SuppressWarnings("static-method")
public class SampleConfig {
	/**
	 * @return the sftp {@link ConnectionDetails}s.
	 */
	@Bean
	public ConnectionDetails SftpConnectionDetails() {
		final ConnectionDetails bean = new ConnectionDetails(
				"sftp://192.168.1.234:22");
		bean.setUserName("userName");
		bean.setPassword("password".toCharArray());
		return bean;
	}

	/**
	 * @return the {@link SftpDownload}.
	 */
	@Bean
	public SftpDownload SftpDownload() {
		final SftpDownload bean = new SftpDownload();
		return bean;
	}

	/**
	 * @return the {@link SftpGateway}.
	 */
	@Bean
	public SftpGateway SftpGateway() {
		final SftpRemoteFileTemplate template = this.SftpRemoteFileTemplate();
		final SftpGateway bean = new SftpGatewayImpl();
		bean.setTemplate(template);
		return bean;
	}

	/**
	 * @return the {@link SftpRemoteFileTemplate}.
	 */
	@Bean
	public SftpRemoteFileTemplate SftpRemoteFileTemplate() {
		final ConnectionDetails cd = this.SftpConnectionDetails();
		final DefaultSftpSessionFactory sf = SftpSessionFactoryProvider
				.getSessionFactory(cd);
		final SftpRemoteFileTemplate bean = new SftpRemoteFileTemplate(sf);
		bean.setRemoteDirectoryExpression(new SpelExpressionParser()
				.parseExpression("headers.directory"));
		return bean;
	}

	/**
	 * @return the {@link SftpUpload}.
	 */
	@Bean
	public SftpUpload SftpUpload() {
		final SftpUpload bean = new SftpUpload();
		return bean;
	}
}
