CREATE SEQUENCE IF NOT EXISTS seq_habit_frequency_generator START WITH 1 INCREMENT BY 50 CACHE 1;

CREATE TABLE IF NOT EXISTS public.habit_frequency
(
    id   BIGINT PRIMARY KEY DEFAULT NEXTVAL('seq_habit_frequency_generator'),
    name VARCHAR(100) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS seq_habit_generator START WITH 1 INCREMENT BY 50 CACHE 1;

CREATE TABLE IF NOT EXISTS public.habit
(
    id           BIGINT PRIMARY KEY DEFAULT NEXTVAL('seq_habit_generator'),
    name         VARCHAR(100) NOT NULL,
    frequency_id INT          NOT NULL,
    begin        DATE         NOT NULL,
    description  VARCHAR(255),
    CONSTRAINT fk_habit_habit_frequency FOREIGN KEY (frequency_id) REFERENCES habit_frequency (id)
);
