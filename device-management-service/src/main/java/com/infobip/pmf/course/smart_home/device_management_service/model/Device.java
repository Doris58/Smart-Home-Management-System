package com.infobip.pmf.course.smart_home.device_management_service.model;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;

@Entity  
@Table(name = "devices") // specifies the db table name explicitly; default: Device
public class Device
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    private String name;
    private String status;
    private String location;

    // one-to-many rel between the Device entity and the UserDeviceAssociation entity
    @OneToMany(mappedBy = "deviceId", cascade = CascadeType.ALL)
    // casc ALL => if a device is deleted, all its associations will be deleted as well

    // the collection of UserDeviceAssociation entries linked to this device
    // initialize the userAssociations field to an empty list by default
    private List<UserDeviceAssociation> userAssociations = new ArrayList<>();

    public Device() {}  /* url shortener - url entity */

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public List<UserDeviceAssociation> getUserAssociations() { return userAssociations; }

    public void setUserAssociations(List<UserDeviceAssociation> userAssociations) 
    {
        this.userAssociations = userAssociations;
    }
}
