-- V1__create_initial_schema.sql
-- TheSDEStudio: initial PostgreSQL schema

-- =========================================================
-- 1. USERS
-- Google OIDC identity and application role
-- =========================================================
CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       google_subject VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(320) NOT NULL UNIQUE,
                       display_name VARCHAR(150) NOT NULL,
                       profile_image_url TEXT,
                       role VARCHAR(20) NOT NULL DEFAULT 'LEARNER',
                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT chk_users_role
                           CHECK (role IN ('OWNER', 'LEARNER'))
);

-- =========================================================
-- 2. COURSES
-- Top-level learning content
-- =========================================================
CREATE TABLE courses (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         title VARCHAR(200) NOT NULL,
                         slug VARCHAR(220) NOT NULL UNIQUE,
                         description TEXT,
                         display_order INTEGER NOT NULL DEFAULT 0,
                         status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
                         created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT chk_courses_status
                             CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
                         CONSTRAINT chk_courses_display_order
                             CHECK (display_order >= 0)
);

-- =========================================================
-- 3. TOPICS
-- Each topic belongs to one course
-- =========================================================
CREATE TABLE topics (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        course_id UUID NOT NULL,
                        title VARCHAR(200) NOT NULL,
                        slug VARCHAR(220) NOT NULL,
                        description TEXT,
                        display_order INTEGER NOT NULL DEFAULT 0,
                        status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
                        created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT fk_topics_course
                            FOREIGN KEY (course_id) REFERENCES courses(id)
                                ON DELETE CASCADE,
                        CONSTRAINT uq_topics_course_slug
                            UNIQUE (course_id, slug),
                        CONSTRAINT chk_topics_status
                            CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
                        CONSTRAINT chk_topics_display_order
                            CHECK (display_order >= 0)
);

CREATE INDEX idx_topics_course_order
    ON topics (course_id, display_order);

-- =========================================================
-- 4. SUBTOPICS
-- Markdown lesson content and optional YouTube URL
-- =========================================================
CREATE TABLE subtopics (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           topic_id UUID NOT NULL,
                           title VARCHAR(200) NOT NULL,
                           slug VARCHAR(220) NOT NULL,
                           content_markdown TEXT NOT NULL DEFAULT '',
                           youtube_url TEXT,
                           display_order INTEGER NOT NULL DEFAULT 0,
                           status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
                           created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_subtopics_topic
                               FOREIGN KEY (topic_id) REFERENCES topics(id)
                                   ON DELETE CASCADE,
                           CONSTRAINT uq_subtopics_topic_slug
                               UNIQUE (topic_id, slug),
                           CONSTRAINT chk_subtopics_status
                               CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
                           CONSTRAINT chk_subtopics_display_order
                               CHECK (display_order >= 0)
);

CREATE INDEX idx_subtopics_topic_order
    ON subtopics (topic_id, display_order);

-- =========================================================
-- 5. USER_SUBTOPICS
-- Per-user notes and learning progress in one table
-- =========================================================
CREATE TABLE user_subtopics (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                user_id UUID NOT NULL,
                                subtopic_id UUID NOT NULL,
                                personal_notes VARCHAR(10000),
                                visited BOOLEAN NOT NULL DEFAULT FALSE,
                                revisit_later BOOLEAN NOT NULL DEFAULT FALSE,
                                last_visited_at TIMESTAMPTZ,
                                created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_user_subtopics_user
                                    FOREIGN KEY (user_id) REFERENCES users(id)
                                        ON DELETE CASCADE,
                                CONSTRAINT fk_user_subtopics_subtopic
                                    FOREIGN KEY (subtopic_id) REFERENCES subtopics(id)
                                        ON DELETE CASCADE,
                                CONSTRAINT uq_user_subtopics_user_subtopic
                                    UNIQUE (user_id, subtopic_id)
);

CREATE INDEX idx_user_subtopics_user_revisit
    ON user_subtopics (user_id, revisit_later)
    WHERE revisit_later = TRUE;

CREATE INDEX idx_user_subtopics_user_visited
    ON user_subtopics (user_id, visited);

-- =========================================================
-- 6. PRACTICE_QUESTIONS
-- Questions associated with a specific subtopic
-- =========================================================
CREATE TABLE practice_questions (
                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                    subtopic_id UUID NOT NULL,
                                    question_markdown TEXT NOT NULL,
                                    answer_markdown TEXT,
                                    difficulty VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
                                    display_order INTEGER NOT NULL DEFAULT 0,
                                    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
                                    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                    CONSTRAINT fk_practice_questions_subtopic
                                        FOREIGN KEY (subtopic_id) REFERENCES subtopics(id)
                                            ON DELETE CASCADE,
                                    CONSTRAINT chk_practice_questions_difficulty
                                        CHECK (difficulty IN ('EASY', 'MEDIUM', 'HARD')),
                                    CONSTRAINT chk_practice_questions_status
                                        CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
                                    CONSTRAINT chk_practice_questions_display_order
                                        CHECK (display_order >= 0)
);

CREATE INDEX idx_practice_questions_subtopic_order
    ON practice_questions (subtopic_id, status, display_order);

-- =========================================================
-- 7. INTERVIEW_QUESTIONS
-- Independent of the course/topic/subtopic hierarchy
-- =========================================================
CREATE TABLE interview_questions (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                     category VARCHAR(100) NOT NULL,
                                     question_markdown TEXT NOT NULL,
                                     answer_markdown TEXT,
                                     difficulty VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
                                     display_order INTEGER NOT NULL DEFAULT 0,
                                     status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
                                     created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                     CONSTRAINT chk_interview_questions_difficulty
                                         CHECK (difficulty IN ('EASY', 'MEDIUM', 'HARD')),
                                     CONSTRAINT chk_interview_questions_status
                                         CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
                                     CONSTRAINT chk_interview_questions_display_order
                                         CHECK (display_order >= 0)
);

CREATE INDEX idx_interview_questions_category
    ON interview_questions (category, status, display_order);