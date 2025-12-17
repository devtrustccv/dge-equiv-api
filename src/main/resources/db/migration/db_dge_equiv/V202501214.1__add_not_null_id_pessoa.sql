DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_requerente'
          AND column_name = 'id_pessoa'
          AND is_nullable = 'YES'
    ) THEN
ALTER TABLE eqv_t_requerente
    ALTER COLUMN id_pessoa SET NOT NULL;
END IF;
END $$;
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'eqv_t_requerente_const_fk'
    ) THEN
ALTER TABLE eqv_t_requerente
    ADD CONSTRAINT eqv_t_requerente_const_fk UNIQUE (id_pessoa);
END IF;
END $$;

