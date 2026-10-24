package com.thesdestudio.learning.subtopic;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseSlug}/topics/{topicSlug}/subtopics")
public class SubtopicController {
    private final SubtopicService subtopicService;

    public SubtopicController(SubtopicService subtopicService) {
        this.subtopicService = subtopicService;
    }

    @GetMapping
    public ResponseEntity<List<SubtopicResponse>> getPublishedSubtopics(@PathVariable String courseSlug, @PathVariable String topicSlug) {
        List<SubtopicResponse> subtopics = subtopicService.getPublishedSubtopics(courseSlug, topicSlug)
                .stream()
                .map(SubtopicResponse::from)
                .toList();
        return ResponseEntity.ok(subtopics);
    }

    @GetMapping("/{subtopicSlug}")
    public ResponseEntity<SubtopicResponse> getPublishedSubtopic(
            @PathVariable String courseSlug,
            @PathVariable String topicSlug,
            @PathVariable String subtopicSlug
    ) {
        SubtopicResponse subtopic = SubtopicResponse.from(
                subtopicService.getPublishedSubtopic(courseSlug, topicSlug, subtopicSlug)
        );
        return ResponseEntity.ok(subtopic);
    }
}
