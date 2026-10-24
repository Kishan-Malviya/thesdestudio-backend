package com.thesdestudio.practice;

import com.thesdestudio.common.enums.ContentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PracticeQuestionService {
    private final PracticeQuestionRepository practiceQuestionRepository;

    public PracticeQuestionService(PracticeQuestionRepository practiceQuestionRepository) {
        this.practiceQuestionRepository = practiceQuestionRepository;
    }

    public List<PracticeQuestion> getPublishedQuestions(UUID subtopicId) {
        return practiceQuestionRepository.findBySubtopic_IdAndStatusOrderByDisplayOrderAsc(
                subtopicId, ContentStatus.PUBLISHED
        );
    }
}
