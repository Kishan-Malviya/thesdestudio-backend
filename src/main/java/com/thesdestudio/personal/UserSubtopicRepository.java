package com.thesdestudio.personal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSubtopicRepository extends JpaRepository<UserSubtopic, UUID> {

    Optional<UserSubtopic> findByUser_IdAndSubtopic_Id(UUID userId, UUID subtopicId);

    List<UserSubtopic> findByUser_IdAndRevisitLaterTrue(UUID userId);

    List<UserSubtopic> findByUser_IdAndVisitedTrue(UUID userId);
}
