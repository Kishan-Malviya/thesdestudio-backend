package com.thesdestudio.interview;

import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.common.enums.Difficulty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class InterviewQuestionService {
    private final InterviewQuestionRepository interviewQuestionRepository;

    public InterviewQuestionService(InterviewQuestionRepository interviewQuestionRepository) {
        this.interviewQuestionRepository = interviewQuestionRepository;
    }

    public List<InterviewQuestion> getPublishedInterviewQuestions(String category, Difficulty difficulty) {
        boolean hasCategory = category != null && !category.isBlank();
        boolean hasDifficulty = difficulty != null;

        if (hasCategory && hasDifficulty) {
            return interviewQuestionRepository.findByCategoryIgnoreCaseAndDifficultyAndStatusOrderByDisplayOrderAsc(
                    category, difficulty, ContentStatus.PUBLISHED
            );
        }

        if (hasCategory) {
            return interviewQuestionRepository.findByCategoryIgnoreCaseAndStatusOrderByDisplayOrderAsc(
                    category, ContentStatus.PUBLISHED
            );
        }

        if (hasDifficulty) {
            return interviewQuestionRepository.findByDifficultyAndStatusOrderByDisplayOrderAsc(
                    difficulty, ContentStatus.PUBLISHED
            );
        }

        return interviewQuestionRepository.findByStatusOrderByDisplayOrderAsc(
                ContentStatus.PUBLISHED
        );
    }
}
