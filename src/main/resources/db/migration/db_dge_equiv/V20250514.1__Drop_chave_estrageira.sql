DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'eqv_t_documento_id_etapa_fkey'
          AND table_name = 'eqv_t_documento'
    ) THEN
ALTER TABLE eqv_t_documento
DROP CONSTRAINT eqv_t_documento_id_etapa_fkey;
END IF;
END
$$;
