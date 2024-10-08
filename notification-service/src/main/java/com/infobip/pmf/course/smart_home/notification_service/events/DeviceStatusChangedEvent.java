package com.infobip.pmf.course.smart_home.notification_service.events;

import java.util.List;
import java.io.Serializable;

// carries information about the device change
public class DeviceStatusChangedEvent implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long deviceId;
    private String deviceName;
    private String newStatus;
    //private List<Long> userIds;
    //private List<String> userEmails;

    // I CAN SAVE HERE THE PREVIOUS STATUS ALSO !!! TO INFORM ABOUT THE CHANGE
    private String oldStatus;

    // Default constructor - creates an empty object
    public DeviceStatusChangedEvent() {}

    // Parameterized constructor
    public DeviceStatusChangedEvent(Long deviceId, String deviceName, String newStatus, String oldStatus) 
    {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.newStatus = newStatus;
        this.oldStatus = oldStatus;
    }

    // Getters and setters
    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }

    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getNewStatus() 
    {
        return newStatus;
    }

    public void setNewStatus(String newStatus) 
    {
        this.newStatus = newStatus;
    }

    public String getOldStatus() 
    {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) 
    {
        this.oldStatus = oldStatus;
    }
}
