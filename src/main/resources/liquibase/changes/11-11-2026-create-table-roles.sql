--liquibase formatted sql

--changeset fatima:11-11-2026-create-roles
CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       role_name VARCHAR(50) NOT NULL,
                       user_id BIGINT NOT NULL,
                       CONSTRAINT fk_roles_user FOREIGN KEY (user_id) REFERENCES users (id)
);
--rollback DROP TABLE public.roles;