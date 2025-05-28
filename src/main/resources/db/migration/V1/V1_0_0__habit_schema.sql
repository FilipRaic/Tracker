create table if not exists public.habit_frequency
(
    id              int primary key,
    name            varchar(100) not null
);

-- TODO: Add user_id as part of later ticket
create table if not exists public.habit
(
    id              bigint primary key,
    name            varchar(100) not null,
    frequency_id    int not null,
    begin           date not null,
    description     varchar(255),
    constraint fk_habit_habit_frequency
        foreign key (frequency_id)
        references habit_frequency (id)
);
