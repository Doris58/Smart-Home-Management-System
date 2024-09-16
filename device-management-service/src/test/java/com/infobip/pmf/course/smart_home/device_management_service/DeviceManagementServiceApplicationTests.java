package com.infobip.pmf.course.smart_home.device_management_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.infobip.pmf.course.smart_home.device_management_service.feignclient.UserClient;
import com.infobip.pmf.course.smart_home.device_management_service.model.Device;
import com.infobip.pmf.course.smart_home.device_management_service.repository.DeviceRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DeviceManagementServiceApplicationTests 
{
	@Autowired
    private DeviceRepository deviceRepository;

	// mock the UserClient to avoid actual HTTP calls during the test
	@MockBean
    private UserClient userClient;

	@Test
	void contextLoads() 
	{

	}

	/* 
	@Test
    void testDatabaseConnection() 
	{
       	Device device = new Device();
        device.setName("Test Device");
        device.setStatus("OFF");
		device.setLocation("Kitchen");
        
		deviceRepository.save(device);

        assertThat(deviceRepository.findAll()).isNotEmpty();
    }
	*/

}
