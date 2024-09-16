package com.infobip.pmf.course.smart_home.user_management_service.model;

public class DeviceDTO 
{
    private Long id;
    private String name;
    private String status;
    private String location;

    // Getters and Setters

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }
}
