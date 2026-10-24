package com.thesdestudio.personal;

import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.common.exception.ResourceNotFoundException;
import com.thesdestudio.learning.subtopic.Subtopic;
import com.thesdestudio.learning.subtopic.SubtopicRepository;
import com.thesdestudio.user.User;
import com.thesdestudio.user.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class PersonalNotesService {
    private final UserRepository userRepository;
    private final SubtopicRepository subtopicRepository;
    private final UserSubtopicRepository userSubtopicRepository;

    public PersonalNotesService(
            UserRepository userRepository,
            SubtopicRepository subtopicRepository,
            UserSubtopicRepository userSubtopicRepository
    ) {
        this.userRepository = userRepository;
        this.subtopicRepository = subtopicRepository;
        this.userSubtopicRepository = userSubtopicRepository;
    }

    @Transactional
    public UserSubtopic updateNotes(
            String googleSubject,
            UUID subtopicId,
            String notes
    ) {
        User user = userRepository.findByGoogleSubject(googleSubject)
                .orElseThrow(() -> new UsernameNotFoundException("Application user not found"));

        Subtopic subtopic = subtopicRepository.findById(subtopicId)
                .filter(s -> s.getStatus() == ContentStatus.PUBLISHED)
                .filter(s -> s.getTopic().getStatus() == ContentStatus.PUBLISHED)
                .filter(s -> s.getTopic().getCourse().getStatus() == ContentStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Published subtopic not found."));

        UserSubtopic userSubtopic = userSubtopicRepository.findByUser_IdAndSubtopic_Id(user.getId(), subtopicId)
                .orElseGet(() -> {
                    UserSubtopic newRecord = new UserSubtopic();
                    newRecord.setUser(user);
                    newRecord.setSubtopic(subtopic);
                    return newRecord;
                });

        userSubtopic.setPersonalNotes(notes);

        return userSubtopicRepository.save(userSubtopic);
    }

    @Transactional
    public UserSubtopic updateVisited(
            String googleSubject,
            UUID subtopicId,
            boolean visited
    ) {
        User user = userRepository.findByGoogleSubject(googleSubject)
                .orElseThrow(() -> new UsernameNotFoundException("Application user not found"));

        Subtopic subtopic = subtopicRepository.findById(subtopicId)
                .filter(s -> s.getStatus() == ContentStatus.PUBLISHED)
                .filter(s -> s.getTopic().getStatus() == ContentStatus.PUBLISHED)
                .filter(s -> s.getTopic().getCourse().getStatus() == ContentStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Published subtopic not found."));

        UserSubtopic userSubtopic = userSubtopicRepository.findByUser_IdAndSubtopic_Id(user.getId(), subtopicId)
                .orElseGet(() -> {
                    UserSubtopic newRecord = new UserSubtopic();
                    newRecord.setUser(user);
                    newRecord.setSubtopic(subtopic);
                    return newRecord;
                });

        userSubtopic.setVisited(visited);

        if (visited) {
            userSubtopic.setLastVisitedAt(Instant.now());
        }

        return userSubtopicRepository.save(userSubtopic);
    }

    @Transactional
    public UserSubtopic updateRevisitLater(
            String googleSubject,
            UUID subtopicId,
            boolean revisitLater
    ) {
        User user = userRepository.findByGoogleSubject(googleSubject)
                .orElseThrow(() -> new UsernameNotFoundException("Application user not found"));

        Subtopic subtopic = subtopicRepository.findById(subtopicId)
                .filter(s -> s.getStatus() == ContentStatus.PUBLISHED)
                .filter(s -> s.getTopic().getStatus() == ContentStatus.PUBLISHED)
                .filter(s -> s.getTopic().getCourse().getStatus() == ContentStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Published subtopic not found."));

        UserSubtopic userSubtopic = userSubtopicRepository.findByUser_IdAndSubtopic_Id(user.getId(), subtopicId)
                .orElseGet(() -> {
                    UserSubtopic newRecord = new UserSubtopic();
                    newRecord.setUser(user);
                    newRecord.setSubtopic(subtopic);
                    return newRecord;
                });

        userSubtopic.setRevisitLater(revisitLater);

        return userSubtopicRepository.save(userSubtopic);
    }

    @Transactional(readOnly = true)
    public List<PersonalSubtopicResponse> getVisited(String googleSubject) {
        User user = userRepository.findByGoogleSubject(googleSubject)
                .orElseThrow(() -> new UsernameNotFoundException("Application user not found"));

        return userSubtopicRepository.findByUser_IdAndVisitedTrue(user.getId())
                .stream()
                .filter(this::isPublishedHierarchy)
                .map(PersonalSubtopicResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PersonalSubtopicResponse> getRevisitLater(String googleSubject) {
        User user = userRepository.findByGoogleSubject(googleSubject)
                .orElseThrow(() -> new UsernameNotFoundException("Application user not found"));

        return userSubtopicRepository.findByUser_IdAndRevisitLaterTrue(user.getId())
                .stream()
                .filter(this::isPublishedHierarchy)
                .map(PersonalSubtopicResponse::from)
                .toList();
    }

    private boolean isPublishedHierarchy(UserSubtopic userSubtopic) {
        var subtopic = userSubtopic.getSubtopic();
        var topic = subtopic.getTopic();
        var course = topic.getCourse();

        return subtopic.getStatus() == ContentStatus.PUBLISHED
                && topic.getStatus() == ContentStatus.PUBLISHED
                && course.getStatus() == ContentStatus.PUBLISHED;
    }
}
