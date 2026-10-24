package com.thesdestudio.learning.subtopic;

import com.thesdestudio.common.enums.ContentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubtopicRepository extends JpaRepository<Subtopic, UUID> {

    List<Subtopic> findByTopic_IdOrderByDisplayOrderAsc(UUID topicId);

    Optional<Subtopic> findByTopic_IdAndSlug(UUID topicId, String slug);

    List<Subtopic> findByTopic_IdAndStatusOrderByDisplayOrderAsc(UUID topicId, ContentStatus status);
}
