package com.thesdestudio.interview;

import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.common.enums.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, UUID> {

    List<InterviewQuestion> findByCategoryIgnoreCaseAndDifficultyAndStatusOrderByDisplayOrderAsc(
            String category, Difficulty difficulty, ContentStatus contentStatus
    );

    List<InterviewQuestion> findByStatusOrderByDisplayOrderAsc(ContentStatus contentStatus);

    List<InterviewQuestion> findByCategoryIgnoreCaseAndStatusOrderByDisplayOrderAsc(
            String category, ContentStatus status);

    List<InterviewQuestion> findByDifficultyAndStatusOrderByDisplayOrderAsc(
            Difficulty difficulty, ContentStatus status);
}
