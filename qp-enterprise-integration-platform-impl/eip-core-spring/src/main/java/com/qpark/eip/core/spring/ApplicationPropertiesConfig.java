package com.qpark.eip.core.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Use System.setProperty("spring.profiles.active", "xxx"); to use the feature
 * cascading.
 *
 * @author bhausen
 */
@Configuration
public class ApplicationPropertiesConfig {
	/** @return the {@link ApplicationPlaceholderConfigurer}. */
	@Bean
	public static ApplicationPlaceholderConfigurer getApplicationPlaceholderConfigurer() {
		ApplicationPlaceholderConfigurer bean = new ApplicationPlaceholderConfigurer();
		List<Resource> locations = new ArrayList<>();
		locations.add(new ClassPathResource("application.properties"));
		if (!System.getProperty("spring.profiles.active", "").equals("")) {
			String[] parts = System.getProperty("spring.profiles.active")
					.split(",");
			Arrays.asList(parts).stream().filter(s -> Objects.nonNull(s))
					.filter(s -> s.trim().length() != 0)
					.forEach(s0 -> Arrays.asList(s0.split(" ")).stream()
							.filter(s -> s.trim().length() != 0)
							.forEach(s -> locations
									.add(new ClassPathResource(String.format(
											"application-%s.properties", s)))));
		}
		bean.setLocations(locations.toArray(new Resource[locations.size()]));
		return bean;
	}
}
