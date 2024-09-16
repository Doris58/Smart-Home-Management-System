package com.infobip.pmf.course.smart_home.api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

import com.infobip.pmf.course.smart_home.api_gateway.feignclient.UserClient;

@SpringBootTest(classes = ApiGatewayApplication.class)
//@EnableAutoConfiguration(exclude={FeignAutoConfiguration.class})
class ApiGatewayApplicationTests 
{
	// mock the UserClient to avoid actual HTTP calls during the test
	@MockBean
    private UserClient userClient;

	@Test
	void contextLoads() {
	}

}
