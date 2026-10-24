package com.thesdestudio.owner.subtopic;

import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.learning.course.CourseRepository;
import com.thesdestudio.learning.subtopic.Subtopic;
import com.thesdestudio.learning.subtopic.SubtopicRepository;
import com.thesdestudio.learning.topic.Topic;
import com.thesdestudio.learning.topic.TopicRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
public class SubtopicManagementService {

    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final SubtopicRepository subtopicRepository;

    public SubtopicManagementService(
            CourseRepository courseRepository,
            TopicRepository topicRepository,
            SubtopicRepository subtopicRepository) {
        this.courseRepository = courseRepository;
        this.topicRepository = topicRepository;
        this.subtopicRepository = subtopicRepository;
    }

    public OwnerSubtopicResponse createSubtopic(
            UUID courseId,
            UUID topicId,
            CreateSubtopicRequest request) {

        getTopic(courseId, topicId);

        Subtopic subtopic = new Subtopic();
        subtopic.setTopic(topicRepository.findById(topicId).orElseThrow());
        subtopic.setTitle(request.title().trim());
        subtopic.setSlug(normalizeSlug(
                request.slug() == null || request.slug().isBlank()
                        ? request.title()
                        : request.slug()));
        subtopic.setContentMarkdown(request.contentMarkdown());
        subtopic.setYoutubeUrl(normalizeYoutubeUrl(request.youtubeUrl()));
        subtopic.setDisplayOrder(
                request.displayOrder() == null ? 0 : request.displayOrder());
        subtopic.setStatus(ContentStatus.DRAFT);

        ensureSlugAvailable(topicId, subtopic.getSlug(), null);

        return OwnerSubtopicResponse.from(subtopicRepository.save(subtopic));
    }

    @Transactional(readOnly = true)
    public List<OwnerSubtopicResponse> getAllSubtopics(
            UUID courseId, UUID topicId) {

        getTopic(courseId, topicId);

        return subtopicRepository
                .findByTopic_IdOrderByDisplayOrderAsc(topicId)
                .stream()
                .sorted(Comparator
                        .comparing(Subtopic::getDisplayOrder,
                                Comparator.nullsFirst(Integer::compareTo))
                        .thenComparing(Subtopic::getTitle))
                .map(OwnerSubtopicResponse::from)
                .toList();
    }

    public OwnerSubtopicResponse updateSubtopic(
            UUID courseId,
            UUID topicId,
            UUID subtopicId,
            UpdateSubtopicRequest request) {

        Subtopic subtopic = getSubtopic(courseId, topicId, subtopicId);

        String slug = normalizeSlug(request.slug());
        ensureSlugAvailable(topicId, slug, subtopicId);

        subtopic.setTitle(request.title().trim());
        subtopic.setSlug(slug);
        subtopic.setContentMarkdown(request.contentMarkdown());
        subtopic.setYoutubeUrl(normalizeYoutubeUrl(request.youtubeUrl()));

        if (request.displayOrder() != null) {
            subtopic.setDisplayOrder(request.displayOrder());
        }

        return OwnerSubtopicResponse.from(subtopicRepository.save(subtopic));
    }

    public OwnerSubtopicResponse updateStatus(
            UUID courseId,
            UUID topicId,
            UUID subtopicId,
            ContentStatus status) {

        Subtopic subtopic = getSubtopic(courseId, topicId, subtopicId);

        if (status == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Status is required");
        }

        subtopic.setStatus(status);
        return OwnerSubtopicResponse.from(subtopicRepository.save(subtopic));
    }

    private Topic getTopic(UUID courseId, UUID topicId) {
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

        return topic;
    }

    private Subtopic getSubtopic(
            UUID courseId, UUID topicId, UUID subtopicId) {

        getTopic(courseId, topicId);

        Subtopic subtopic = subtopicRepository.findById(subtopicId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Subtopic not found"));

        if (!subtopic.getTopic().getId().equals(topicId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Subtopic not found in this topic");
        }

        return subtopic;
    }

    private void ensureSlugAvailable(
            UUID topicId, String slug, UUID currentSubtopicId) {

        subtopicRepository.findByTopic_IdAndSlug(topicId, slug)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(currentSubtopicId)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "A subtopic with this slug already exists");
                    }
                });
    }

    private String normalizeSlug(String value) {
        String slug = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        if (slug.isBlank() || slug.length() > 220) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid slug");
        }

        return slug;
    }

    private String normalizeYoutubeUrl(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }

        String normalized = url.trim();

        if (!normalized.startsWith("https://www.youtube.com/")
                && !normalized.startsWith("https://youtube.com/")
                && !normalized.startsWith("https://youtu.be/")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Provide a valid HTTPS YouTube URL");
        }

        return normalized;
    }
}