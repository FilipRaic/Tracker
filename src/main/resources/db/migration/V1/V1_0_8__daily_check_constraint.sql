ALTER TABLE IF EXISTS daily_check
    DROP CONSTRAINT IF EXISTS uk_daily_check_date;

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1
                       FROM information_schema.table_constraints
                       WHERE constraint_name = 'uk_user_daily_check_date'
                         AND table_name = 'daily_check'
                         AND table_schema = 'public')
        THEN
            ALTER TABLE IF EXISTS daily_check
                ADD CONSTRAINT uk_user_daily_check_date UNIQUE (user_id, check_in_date);
        END IF;
    END
$$;
