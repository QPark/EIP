/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ ConfigA.class })
public class ConfigA2 {
	@Autowired
	private ConfigA configA;

	public ConfigA2() {
	}

	@PostConstruct
	private void init() {
		System.out.println("INIT in ConfigA2");
		this.configA.setA("This the A string set.");
	}
}
