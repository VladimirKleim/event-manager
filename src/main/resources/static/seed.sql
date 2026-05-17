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


DO $$
DECLARE
batch_size integer := 100000;
    total_rows integer := 10000000;
    start_num integer := 1;
    end_num integer;
BEGIN
    WHILE start_num <= total_rows LOOP
        end_num := LEAST(start_num + batch_size - 1, total_rows);

INSERT INTO locations (name, address, capacity, description)
SELECT
    'Location ' || gs,
    'Address ' || gs,
    50 + (random() * 450)::int,
    'Description for location ' || gs
FROM generate_series(start_num, end_num) AS gs;

RAISE NOTICE 'Inserted rows: % - %', start_num, end_num;

        start_num := end_num + 1;
END LOOP;
END $$;

SET key-1 value-1


select lo
