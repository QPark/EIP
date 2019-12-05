/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samples.platform.service.library.test;

import static org.assertj.core.api.Assertions.assertThat;

import javax.xml.bind.JAXBElement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.samples.platform.model.library.BookCriteriaType;
import com.samples.platform.service.library.msg.GetBookRequestType;
import com.samples.platform.service.library.msg.GetBookResponseType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LibraryApplicationIntegrationTests {
	private com.samples.platform.service.library.msg.ObjectFactory of = new com.samples.platform.service.library.msg.ObjectFactory();

	private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

	@LocalServerPort
	private int port = 0;

	@Before
	public void init() throws Exception {
		marshaller.setPackagesToScan(
				ClassUtils.getPackageName(GetBookRequestType.class));
		marshaller.afterPropertiesSet();
	}

	@Test
	public void testSendAndReceive() {
		WebServiceTemplate ws = new WebServiceTemplate(marshaller);
		GetBookRequestType request = new GetBookRequestType();
		request.setCriteria(new BookCriteriaType());
		JAXBElement response = (JAXBElement) ws.marshalSendAndReceive(
				"http://localhost:" + port + "/services/library",
				of.createGetBookRequest(request));
		System.out.println(response);
		assertThat(response).isNotNull();
		System.out.println(((GetBookResponseType) response.getValue()).getBook()
				.get(0).getTitle());
	}
}