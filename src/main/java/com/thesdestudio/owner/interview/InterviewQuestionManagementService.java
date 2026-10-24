package com.thesdestudio.owner.interview;

import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.interview.InterviewQuestion;
import com.thesdestudio.interview.InterviewQuestionRepository;
import com.thesdestudio.interview.InterviewQuestionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class InterviewQuestionManagementService {

    private final InterviewQuestionRepository repository;

    public InterviewQuestionManagementService(
            InterviewQuestionRepository repository) {
        this.repository = repository;
    }

    public InterviewQuestionResponse create(
            CreateInterviewQuestionRequest request) {

        InterviewQuestion question = new InterviewQuestion();
        question.setCategory(normalizeCategory(request.category()));
        question.setQuestionMarkdown(request.questionMarkdown().trim());
        question.setAnswerMarkdown(request.answerMarkdown());
        question.setDifficulty(request.difficulty());
        question.setDisplayOrder(
                request.displayOrder() == null ? 0 : request.displayOrder());
        question.setStatus(ContentStatus.DRAFT);

        return InterviewQuestionResponse.from(repository.save(question));
    }

    @Transactional(readOnly = true)
    public List<InterviewQuestionResponse> getAll(
            String category,
            com.thesdestudio.common.enums.Difficulty difficulty,
            ContentStatus status) {

        List<InterviewQuestion> questions = repository.findAll();

        return questions.stream()
                .filter(q -> category == null || category.isBlank()
                        || q.getCategory().equalsIgnoreCase(category.trim()))
                .filter(q -> difficulty == null || q.getDifficulty() == difficulty)
                .filter(q -> status == null || q.getStatus() == status)
                .sorted(Comparator
                        .comparing(InterviewQuestion::getDisplayOrder,
                                Comparator.nullsFirst(Integer::compareTo))
                        .thenComparing(InterviewQuestion::getCategory,
                                String.CASE_INSENSITIVE_ORDER))
                .map(InterviewQuestionResponse::from)
                .toList();
    }

    public InterviewQuestionResponse update(
            UUID questionId,
            UpdateInterviewQuestionRequest request) {

        InterviewQuestion question = getQuestion(questionId);

        question.setCategory(normalizeCategory(request.category()));
        question.setQuestionMarkdown(request.questionMarkdown().trim());
        question.setAnswerMarkdown(request.answerMarkdown());
        question.setDifficulty(request.difficulty());

        if (request.displayOrder() != null) {
            question.setDisplayOrder(request.displayOrder());
        }

        return InterviewQuestionResponse.from(repository.save(question));
    }

    public InterviewQuestionResponse updateStatus(
            UUID questionId, ContentStatus status) {

        InterviewQuestion question = getQuestion(questionId);

        if (status == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Status is required");
        }

        question.setStatus(status);
        return InterviewQuestionResponse.from(repository.save(question));
    }

    private InterviewQuestion getQuestion(UUID questionId) {
        return repository.findById(questionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Interview question not found"));
    }

    private String normalizeCategory(String category) {
        String normalized = category.trim().replaceAll("\\s+", " ");

        if (normalized.isBlank() || normalized.length() > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid category");
        }

        return normalized;
    }
}
