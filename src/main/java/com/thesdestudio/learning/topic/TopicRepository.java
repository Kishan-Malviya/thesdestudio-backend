package com.thesdestudio.learning.topic;

import com.thesdestudio.common.enums.ContentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TopicRepository extends JpaRepository<Topic, UUID> {

    List<Topic> findByCourse_IdOrderByDisplayOrderAsc(UUID courseId);

    Optional<Topic> findByCourse_IdAndSlug(UUID courseId, String slug);

    List<Topic> findByCourse_IdAndStatusOrderByDisplayOrderAsc(UUID courseId, ContentStatus status);
}
