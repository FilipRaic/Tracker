CREATE SEQUENCE IF NOT EXISTS seq_question_generator START WITH 1 INCREMENT BY 50 CACHE 1;

CREATE TABLE IF NOT EXISTS public.question
(
    id         BIGINT PRIMARY KEY DEFAULT NEXTVAL('seq_question_generator'),
    category   VARCHAR(50)  NOT NULL,
    content    VARCHAR(255) NOT NULL,
    content_de VARCHAR(255) NOT NULL,
    content_hr VARCHAR(255) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS seq_daily_check_generator START WITH 1 INCREMENT BY 50 CACHE 1;

CREATE TABLE IF NOT EXISTS public.daily_check
(
    id            BIGINT PRIMARY KEY DEFAULT NEXTVAL('seq_daily_check_generator'),
    uuid          UUID      NOT NULL,
    check_in_date DATE      NOT NULL,
    created_at    TIMESTAMP NOT NULL,
    completed_at  TIMESTAMP,
    completed     BOOLEAN   NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_daily_check_date UNIQUE (check_in_date)
);

CREATE SEQUENCE IF NOT EXISTS seq_daily_question_generator START WITH 1 INCREMENT BY 50 CACHE 1;

CREATE TABLE IF NOT EXISTS public.daily_question
(
    id             BIGINT PRIMARY KEY DEFAULT NEXTVAL('seq_daily_question_generator'),
    category       VARCHAR(50)  NOT NULL,
    content        VARCHAR(255) NOT NULL,
    content_de     VARCHAR(255) NOT NULL,
    content_hr     VARCHAR(255) NOT NULL,
    score          INTEGER,
    daily_check_id BIGINT,
    CONSTRAINT fk_question_daily_check FOREIGN KEY (daily_check_id) REFERENCES daily_check (id)
);
