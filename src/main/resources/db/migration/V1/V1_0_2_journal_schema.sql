-- TODO: Add user_id as part of later ticket
create table if not exists public.journal_entry
(
    id int primary key ,
    description text,
    date date not null
);
