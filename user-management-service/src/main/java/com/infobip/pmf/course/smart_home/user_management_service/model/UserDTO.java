package com.infobip.pmf.course.smart_home.user_management_service.model;

public class UserDTO 
{
    // only non-sensitive information
    // prevents leaking/exposing apiKey, or other sensitive fields in the User entity

    private Long id;
    private String username;
    private String email;
   
    public UserDTO(User user) 
    {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    // Getters and Setters
    public Long getId() 
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