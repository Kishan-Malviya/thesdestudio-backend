package com.thesdestudio.learning;

import com.thesdestudio.common.enums.ContentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubtopicRepository extends JpaRepository<Subtopic, UUID> {

    List<Subtopic> findByTopic_IdOrderByDisplayOrderAsc(UUID topicId);

    List<Subtopic> findByTopic_IdAndStatusOrderByDisplayOrderAsc(UUID topicId, ContentStatus status);
}
