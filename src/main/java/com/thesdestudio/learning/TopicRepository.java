package com.thesdestudio.learning;

import com.thesdestudio.common.enums.ContentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TopicRepository extends JpaRepository<Topic, UUID> {

    List<Topic> findByCourse_IdOrderByDisplayOrderAsc(UUID courseId);

    List<Topic> findByCourse_IdAndStatusOrderByDisplayOrderAsc(UUID courseId, ContentStatus status);
}
