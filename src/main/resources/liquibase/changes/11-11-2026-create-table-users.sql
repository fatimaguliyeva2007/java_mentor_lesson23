--liquibase formatted sql

--changeset fatima:11-11-2026-create-users
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       user_name VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);
--rollback DROP TABLE public.users;