--liquibase formatted sql

--changeset fatima:22-10-2026-insert-speakers
INSERT INTO public.speakers (id, full_name, email, company)
VALUES (1, 'Fatima Guliyeva', 'fatima@gmail.com', 'Matrix');

INSERT INTO public.speakers (id, full_name, email, company)
VALUES (2, 'Aysel Mammadova', 'nicat@gmail.com', 'Tech Corp');