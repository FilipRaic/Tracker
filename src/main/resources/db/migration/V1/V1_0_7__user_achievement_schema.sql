CREATE SEQUENCE IF NOT EXISTS seq_user_achievement_generator START WITH 1 INCREMENT BY 50 CACHE 1;

CREATE TABLE IF NOT EXISTS user_achievement(
    id                 BIGINT PRIMARY KEY DEFAULT NEXTVAL('seq_user_achievement_generator'),
    user_id            BIGINT       NOT NULL,
    achievement_id     BIGINT       NOT NULL,
    completed          BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_user_achievement_user FOREIGN KEY (user_id) REFERENCES application_user (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_achievement_achievement FOREIGN KEY (achievement_id) REFERENCES achievement (id) ON DELETE CASCADE,
    CONSTRAINT uc_user_achievement UNIQUE (user_id, achievement_id)
);
