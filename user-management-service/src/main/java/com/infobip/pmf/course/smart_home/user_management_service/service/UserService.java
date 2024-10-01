package com.infobip.pmf.course.smart_home.user_management_service.service;

import com.infobip.pmf.course.smart_home.user_management_service.feignclient.DeviceClient;
import com.infobip.pmf.course.smart_home.user_management_service.model.DeviceDTO;
import com.infobip.pmf.course.smart_home.user_management_service.model.User;
import com.infobip.pmf.course.smart_home.user_management_service.model.UserDTO;
import com.infobip.pmf.course.smart_home.user_management_service.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.infobip.pmf.course.smart_home.user_management_service.events.UserDeletedEvent;
import com.infobip.pmf.course.smart_home.user_management_service.exception.ResourceNotFoundException;

//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// collect the results of a stream operation into a list or another collection
import java.util.stream.Collectors;

@Service
public class UserService 
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceClient deviceClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void configureRabbitTemplate(RabbitTemplate rabbitTemplate, Jackson2JsonMessageConverter converter) 
    {
        rabbitTemplate.setMessageConverter(converter);
    }

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Create a new user
    public User createUser(User user) 
    {
        // Generate the API key here
        user.setApiKey(generateApiKey());  // setter - ok
        return userRepository.save(user);
    }

    // Generate API key
    private String generateApiKey() 
    {
        // Implement API key generation logic here
        return UUID.randomUUID().toString();
    }

    // Get all users - without APikeys!
    public List<UserDTO> getAllUsers() 
    {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(user))  // Convert User entity to UserDTO
                .collect(Collectors.toList());
    }

    // Get a user by ID - without APikey!
    public UserDTO getUserById(Long id) 
    {
        return userRepository.findById(id)
                .map(user -> new UserDTO(user)) //UserDTO(user.getId(), user.getUsername(), user.getEmail()))
                .orElse(null);
    }

    // Delete a user, publish the UserDeletedEvent
    @Transactional // Ensures that the deletion and event publication occur in a single transaction
    public void deleteUser(Long userId) 
    {
        // fetch associated emails before deletion !
        List<String> associatedEmails = deviceClient.getAssociatedUserEmails(userId);

        // perform user deletion
        userRepository.deleteById(userId);

        // publish the UserDeletedEvent event to RabbitMQ, pass associated emails as part of the event
        UserDeletedEvent event = new UserDeletedEvent(userId, associatedEmails);
        rabbitTemplate.convertAndSend("userExchange", "device.user.deleted", event);
        rabbitTemplate.convertAndSend("userExchange", "notification.user.deleted", event);

        // track when the event is published
        logger.info("Published UserDeletedEvent for userId: {}", userId);
    }

    // Validate an API key
    public boolean validateApiKey(String apiKey) 
    {
        return userRepository.findByApiKey(apiKey).isPresent();
    }

    public boolean checkUserExistsByEmail(String email)
    {
        return userRepository.existsByEmail(email);
    }

    /* --> ambiguity! 
    // Get a user by username - without API key!
    public UserDTO getUserByUsername(String username) 
    {
        return userRepository.findByUsername(username)
                .map(user -> new UserDTO(user)) //UserDTO(user.getId(), user.getUsername(), user.getEmail()))
                .orElse(null);
    }
    */

    /* 
    public Optional<User> findUserByUsername(String username) 
    {
        return userRepository.findByUsername(username);
    }
    */
}




