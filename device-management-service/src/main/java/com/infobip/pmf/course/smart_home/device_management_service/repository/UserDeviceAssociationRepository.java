package com.infobip.pmf.course.smart_home.device_management_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.repository.query.Param;

import com.infobip.pmf.course.smart_home.device_management_service.model.UserDeviceAssociation;

@Repository  // the annotation is not necessary
public interface UserDeviceAssociationRepository extends JpaRepository<UserDeviceAssociation, Long> 
{
    UserDeviceAssociation findByUserIdAndDeviceId(Long userId, Long deviceId);

    // fetch all associations for a user with userId
    List<UserDeviceAssociation> findByUserId(Long userId);

    // fetch userIds associated with a same device as a user with userId
    @Query("SELECT DISTINCT uda.userId FROM UserDeviceAssociation uda WHERE uda.deviceId IN (SELECT ud.deviceId FROM UserDeviceAssociation ud WHERE ud.userId = :userId)")
    List<Long> findAssociatedUserIdsByUserId(@Param("userId") Long userId);
}