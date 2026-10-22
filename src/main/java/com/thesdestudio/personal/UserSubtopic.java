package com.thesdestudio.personal;

import com.thesdestudio.learning.Subtopic;
import com.thesdestudio.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "user_subtopics",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_user_subtopics_user_subtopic",
                        columnNames = {"user_id", "subtopic_id"}
                )
        }
)
public class UserSubtopic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subtopic_id", nullable = false)
    private Subtopic subtopic;

    @Column(name = "personal_notes", length = 10000)
    private String personalNotes;

    @Column(nullable = false)
    private boolean visited = false;

    @Column(name = "revisit_later", nullable = false)
    private boolean revisitLater = false;

    @Column(name = "last_visited_at")
    private Instant lastVisitedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected UserSubtopic() {
        // Required by JPA
    }

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Subtopic getSubtopic() {
        return subtopic;
    }

    public void setSubtopic(Subtopic subtopic) {
        this.subtopic = subtopic;
    }

    public String getPersonalNotes() {
        return personalNotes;
    }

    public void setPersonalNotes(String personalNotes) {
        this.personalNotes = personalNotes;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isRevisitLater() {
        return revisitLater;
    }

    public void setRevisitLater(boolean revisitLater) {
        this.revisitLater = revisitLater;
    }

    public Instant getLastVisitedAt() {
        return lastVisitedAt;
    }

    public void setLastVisitedAt(Instant lastVisitedAt) {
        this.lastVisitedAt = lastVisitedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}