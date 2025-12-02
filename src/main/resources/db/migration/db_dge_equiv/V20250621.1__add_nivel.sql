DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqvt_t_decisao_ap'
          AND column_name = 'nivel'
    ) THEN
ALTER TABLE public.eqvt_t_decisao_ap
    ADD COLUMN nivel INTEGER;
END IF;
END
$$;
