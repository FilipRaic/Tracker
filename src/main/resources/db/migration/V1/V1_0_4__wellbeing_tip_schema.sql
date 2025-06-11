CREATE SEQUENCE IF NOT EXISTS  seq_wellbeing_tip_generator START WITH 1 INCREMENT BY 50 CACHE 1;

CREATE TABLE IF NOT EXISTS public.wellbeing_tip (
    id BIGINT PRIMARY KEY DEFAULT NEXTVAL('seq_wellbeing_tip_generator'),
    category VARCHAR(255) NOT NULL,
    score INTEGER,
    tip_text TEXT
);
