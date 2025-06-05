CREATE SEQUENCE IF NOT EXISTS seq_journal_entry_generator START WITH 1 INCREMENT BY 50 CACHE 1;

CREATE TABLE IF NOT EXISTS public.journal_entry
(
    id          BIGINT PRIMARY KEY DEFAULT NEXTVAL('seq_journal_entry_generator'),
    description TEXT,
    date        DATE NOT NULL
);
