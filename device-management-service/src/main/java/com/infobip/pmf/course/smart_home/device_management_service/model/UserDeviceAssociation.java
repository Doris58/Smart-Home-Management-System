package com.infobip.pmf.course.smart_home.device_management_service.model;

import jakarta.persistence.*;

// a join table in DMS database that maps user IDs to device IDs
@Entity
@Table(name = "user_device")
public class UserDeviceAssociation 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "device_id")
    private Long deviceId;

    public UserDeviceAssociation() {}

    public UserDeviceAssociation(Long userId, Long deviceId) 
    {
        this.userId = userId;
        this.deviceId = deviceId;
    }

    public Long getId() { return id; }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserId(Long userId)
     {
        this.userId = userId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }
}
