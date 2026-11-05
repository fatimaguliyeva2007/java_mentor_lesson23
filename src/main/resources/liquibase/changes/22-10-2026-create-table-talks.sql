--liquibase formatted sql

--changeset fatima:22-10-2026-create-talks
CREATE TABLE talks (
id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    room VARCHAR(255),
    duration_minutes INTEGER,
    scheduled_at TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    speaker_id BIGINT NOT NULL,
    CONSTRAINT fk_talks_speaker FOREIGN KEY (speaker_id) REFERENCES speakers (id)
);
--rollback DROP TABLE public.talks;