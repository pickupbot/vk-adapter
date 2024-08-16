package com.adapter.repository;

import com.adapter.model.DatingProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VkAdapterRepository extends JpaRepository<DatingProfile, Long> {

    @Query("SELECT EXISTS(SELECT p FROM DatingProfile p WHERE p.profileId = :profileId)")
    boolean isDatingProfileExist(long profileId);
}
