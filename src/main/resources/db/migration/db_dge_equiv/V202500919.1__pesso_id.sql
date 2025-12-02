DO $$
BEGIN
    -- Adiciona id_pessoa se não existir
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_requisicao'
          AND column_name = 'id_pessoa'
    ) THEN
ALTER TABLE public.eqv_t_requisicao
    ADD COLUMN id_pessoa INTEGER;
END IF;
END;
$$;
