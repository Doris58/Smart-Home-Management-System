package com.infobip.pmf.course.smart_home.user_management_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.infobip.pmf.course.smart_home.user_management_service.feignclient.DeviceClient;

@SpringBootTest
class UserManagementServiceApplicationTests 
{
	// mock the DeviceClient to avoid actual HTTP calls during the test
	@MockBean
    private DeviceClient deviceClient;

	@Test
	void contextLoads() {
	}

}
