package com.teamviewer.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EcommerceApplicationTests extends AbstractContainerBaseTest {

	@Autowired
	protected TestRestTemplate restTemplate;

}
