DO $$
    BEGIN
        FOR i IN 0..99 LOOP
                INSERT INTO events (name, owner_id, max_place, date, cost, duration, location_id, status)
                SELECT
                        'Event ' || (i * 10000 + seq),
                        (random() * 1000 + 1)::integer,
                        (random() * 500 + 50)::integer,
                        NOW() - (random() * 365 * 10 || ' days')::interval,
                        (random() * 1000 + 50)::integer,
                        (random() * 240 + 30)::integer,
                        10,  -- фиксированная локация 10
                        (random() * 3)::integer  -- 0, 1, 2 (без 3!)
                FROM generate_series(1, 10000) seq;

                RAISE NOTICE 'Вставлено % записей', (i + 1) * 10000;
            END LOOP;
    END $$;
