CREATE SEQUENCE IF NOT EXISTS seq_achievement_generator START WITH 1 INCREMENT BY 50 CACHE 1;

CREATE TABLE IF NOT EXISTS public.achievement (
    id BIGINT PRIMARY KEY DEFAULT NEXTVAL('seq_achievement_generator'),
    name VARCHAR(255) NOT NULL,
    unlock_condition VARCHAR(255) NOT NULL,
    emoji VARCHAR(255),
    description VARCHAR(255) NOT NULL
);
