package com.infobip.pmf.course.smart_home.device_management_service.model;

public class UserDTO 
{
    // only relevant, non-sensitive information
    // => apiKey should remain internal to the user management service

    private Long id;
    private String username;
    private String email;
   
    // Getters and Setters
    public Long getId() // id is read-only here
    {
        return id;
    }

    public String getUsername() 
    {
        return username;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getEmail() 
    {
        return email;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }
}
