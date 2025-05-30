create table if not exists public.habit_completion (
    id          bigint primary key,
    habit_id    bigint not null,
    completion_date date not null,
    done boolean not null,
    constraint fk_completion_habit
        foreign key (habit_id)
        references habit (id),
    constraint uc_habit_day unique (habit_id, completion_date)  -- Prevent duplicate completion
);