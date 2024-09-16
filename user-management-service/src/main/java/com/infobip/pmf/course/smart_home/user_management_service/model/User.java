
package com.infobip.pmf.course.smart_home.user_management_service.model;

import jakarta.persistence.*;

// represents the user data stored in the database
@Entity
@Table(name = "users")
public class User 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String apiKey;
   
    private String email; 

    // Getters and Setters
    public Long getId() { return id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getApiKey() { return apiKey; }

    public void setApiKey(String apiKey) { this.apiKey = apiKey; }  /* DA !!! */

    public void setEmail(String email) { this.email = email; }

    public String getEmail() { return email; }
}

