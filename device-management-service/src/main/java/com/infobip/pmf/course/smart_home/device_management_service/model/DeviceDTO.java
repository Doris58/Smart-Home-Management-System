package com.infobip.pmf.course.smart_home.device_management_service.model;

public class DeviceDTO 
{
    private Long id;
    private String name;
    private String status;
    private String location;

    // Getters and Setters

    public DeviceDTO(Long id, String name, String status, String location) 
    {
        this.id = id;
        this.name = name;
        this.status = status;
        this.location = location;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }
}
