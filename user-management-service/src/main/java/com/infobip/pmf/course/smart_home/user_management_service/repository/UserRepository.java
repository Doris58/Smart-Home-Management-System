package com.infobip.pmf.course.smart_home.user_management_service.repository;

import com.infobip.pmf.course.smart_home.user_management_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository  // the annotation is not necessary
public interface UserRepository extends JpaRepository<User, Long> 
{
    Optional<User> findByUsername(String username);

    Optional<User> findByApiKey(String apiKey);

    // returns a simple boolean without fetching the entire user entity as the findByEmail method
    boolean existsByEmail(String email);
}
