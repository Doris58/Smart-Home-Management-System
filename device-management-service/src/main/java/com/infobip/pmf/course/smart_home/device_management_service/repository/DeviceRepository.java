package com.infobip.pmf.course.smart_home.device_management_service.repository; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infobip.pmf.course.smart_home.device_management_service.model.Device;

@Repository  // the annotation is not necessary
public interface DeviceRepository extends JpaRepository<Device, Long> 
{
    
}

