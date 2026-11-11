--liquibase formatted sql

--changeset fatima:22-10-2026-create-speakers
CREATE TABLE speakers (
                          id BIGSERIAL PRIMARY KEY,
                          full_name VARCHAR(255),
                          email VARCHAR(255) UNIQUE,
                          company VARCHAR(255)
);
--rollback DROP TABLE public.speakers;