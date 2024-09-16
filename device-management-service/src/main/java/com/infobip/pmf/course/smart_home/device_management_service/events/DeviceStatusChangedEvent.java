package com.infobip.pmf.course.smart_home.device_management_service.events;

import java.util.List;

// carries information about the device change
public class DeviceStatusChangedEvent 
{
    private Long deviceId;
    private String deviceName;
    private String newStatus;
    private List<Long> userIds;
    private List<String> userEmails;

    // I CAN SAVE HERE THE PREVIOUS STATUS ALSO !!! TO INFORM ABOUT THE CHANGE
    private String oldStatus;

    // Default constructor - creates an empty object
    public DeviceStatusChangedEvent() {}

    // Parameterized constructor
    public DeviceStatusChangedEvent(Long deviceId, String deviceName, String newStatus, String oldStatus, List<Long> userIds, List<String> userEmails) 
    {
        this.deviceId = deviceId;  // from device
        this.deviceName = deviceName;  // from device
        this.newStatus = newStatus;  // from device
        this.oldStatus = oldStatus;
        this.userIds = userIds;
        this.userEmails = userEmails;
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

    public List<Long> getUserIds() 
    {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) 
    {
        this.userIds = userIds;
    }

    public List<String> getUserEmails() 
    {
        return userEmails;
    }

    public void setUserEmails(List<String> userEmails) 
    {
        this.userEmails = userEmails;
    }
}
