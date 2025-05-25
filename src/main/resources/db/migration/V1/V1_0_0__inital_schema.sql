CREATE TABLE if not exists public.test_table
(
    id          bigint not null,
    description varchar(40)
);

ALTER TABLE if exists public.test_table
    add column if not exists notes varchar(455);
