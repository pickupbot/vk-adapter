package com.adapter.repository;

import com.adapter.model.DatingProfile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface VkAdapterRepository extends JpaRepository<DatingProfile, Long> {

    @Query("SELECT EXISTS(SELECT p FROM DatingProfile p WHERE p.profileId = :profileId)")
    boolean isDatingProfileExist(long profileId);

    @Modifying
    @Transactional
    @Query("DELETE FROM DatingProfile p WHERE p.profileId = :profileId")
    void deleteByProfileId(long profileId);
}
