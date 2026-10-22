package com.thesdestudio.practice;

import com.thesdestudio.common.enums.ContentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PracticeQuestionRepository extends JpaRepository<PracticeQuestion, UUID> {

    List<PracticeQuestion> findBySubtopic_IdAndStatusOrderByDisplayOrderAsc(UUID subtopicId, ContentStatus status);
}
