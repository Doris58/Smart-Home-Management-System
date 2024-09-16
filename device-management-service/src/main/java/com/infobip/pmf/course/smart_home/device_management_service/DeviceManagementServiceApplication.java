package com.infobip.pmf.course.smart_home.device_management_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.infobip.pmf.course.smart_home.device_management_service.feignclient")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class DeviceManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeviceManagementServiceApplication.class, args);
	}

}
