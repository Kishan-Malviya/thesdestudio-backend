package com.thesdestudio.owner.practice;

import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.learning.course.CourseRepository;
import com.thesdestudio.learning.subtopic.Subtopic;
import com.thesdestudio.learning.subtopic.SubtopicRepository;
import com.thesdestudio.learning.topic.Topic;
import com.thesdestudio.learning.topic.TopicRepository;
import com.thesdestudio.practice.PracticeQuestion;
import com.thesdestudio.practice.PracticeQuestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PracticeQuestionManagementService {

    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final SubtopicRepository subtopicRepository;
    private final PracticeQuestionRepository questionRepository;

    public PracticeQuestionManagementService(
            CourseRepository courseRepository,
            TopicRepository topicRepository,
            SubtopicRepository subtopicRepository,
            PracticeQuestionRepository questionRepository) {
        this.courseRepository = courseRepository;
        this.topicRepository = topicRepository;
        this.subtopicRepository = subtopicRepository;
        this.questionRepository = questionRepository;
    }

    public OwnerPracticeQuestionResponse create(
            UUID courseId,
            UUID topicId,
            UUID subtopicId,
            CreatePracticeQuestionRequest request) {

        Subtopic subtopic = getSubtopic(courseId, topicId, subtopicId);

        PracticeQuestion question = new PracticeQuestion();
        question.setSubtopic(subtopic);
        question.setQuestionMarkdown(request.questionMarkdown().trim());
        question.setAnswerMarkdown(request.answerMarkdown());
        question.setDifficulty(request.difficulty());
        question.setDisplayOrder(
                request.displayOrder() == null ? 0 : request.displayOrder());
        question.setStatus(ContentStatus.DRAFT);

        return OwnerPracticeQuestionResponse.from(
                questionRepository.save(question));
    }

    @Transactional(readOnly = true)
    public List<OwnerPracticeQuestionResponse> getAll(
            UUID courseId, UUID topicId, UUID subtopicId) {

        getSubtopic(courseId, topicId, subtopicId);

        return questionRepository
                .findBySubtopic_IdOrderByDisplayOrderAsc(subtopicId)
                .stream()
                .filter(question -> question.getSubtopic().getId().equals(subtopicId))
                .sorted(Comparator
                        .comparing(PracticeQuestion::getDisplayOrder,
                                Comparator.nullsFirst(Integer::compareTo))
                        .thenComparing(PracticeQuestion::getId))
                .map(OwnerPracticeQuestionResponse::from)
                .toList();
    }

    public OwnerPracticeQuestionResponse update(
            UUID courseId,
            UUID topicId,
            UUID subtopicId,
            UUID questionId,
            UpdatePracticeQuestionRequest request) {

        PracticeQuestion question =
                getQuestion(courseId, topicId, subtopicId, questionId);

        question.setQuestionMarkdown(request.questionMarkdown().trim());
        question.setAnswerMarkdown(request.answerMarkdown());
        question.setDifficulty(request.difficulty());

        if (request.displayOrder() != null) {
            question.setDisplayOrder(request.displayOrder());
        }

        return OwnerPracticeQuestionResponse.from(
                questionRepository.save(question));
    }

    public OwnerPracticeQuestionResponse updateStatus(
            UUID courseId,
            UUID topicId,
            UUID subtopicId,
            UUID questionId,
            ContentStatus status) {

        PracticeQuestion question =
                getQuestion(courseId, topicId, subtopicId, questionId);

        if (status == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Status is required");
        }

        question.setStatus(status);

        return OwnerPracticeQuestionResponse.from(
                questionRepository.save(question));
    }

    private Subtopic getSubtopic(
            UUID courseId, UUID topicId, UUID subtopicId) {

        if (!courseRepository.existsById(courseId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Course not found");
        }

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Topic not found"));

        if (!topic.getCourse().getId().equals(courseId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Topic not found in this course");
        }

        Subtopic subtopic = subtopicRepository.findById(subtopicId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Subtopic not found"));

        if (!subtopic.getTopic().getId().equals(topicId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Subtopic not found in this topic");
        }

        return subtopic;
    }

    private PracticeQuestion getQuestion(
            UUID courseId,
            UUID topicId,
            UUID subtopicId,
            UUID questionId) {

        getSubtopic(courseId, topicId, subtopicId);

        PracticeQuestion question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Practice question not found"));

        if (!question.getSubtopic().getId().equals(subtopicId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Practice question not found in this subtopic");
        }

        return question;
    }
}