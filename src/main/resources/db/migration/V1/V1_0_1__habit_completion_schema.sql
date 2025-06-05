CREATE SEQUENCE IF NOT EXISTS seq_habit_completion_generator START WITH 1 INCREMENT BY 50 CACHE 1;

CREATE TABLE IF NOT EXISTS public.habit_completion
(
    id              BIGINT PRIMARY KEY DEFAULT NEXTVAL('seq_habit_completion_generator'),
    habit_id        BIGINT  NOT NULL,
    completion_date DATE    NOT NULL,
    done            BOOLEAN NOT NULL,
    CONSTRAINT fk_completion_habit FOREIGN KEY (habit_id) REFERENCES habit (id),
    CONSTRAINT uc_habit_day UNIQUE (habit_id, completion_date)
);
