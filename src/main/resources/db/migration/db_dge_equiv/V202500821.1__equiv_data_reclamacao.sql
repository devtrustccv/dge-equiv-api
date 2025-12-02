DO $$
BEGIN
    -- Adiciona nu_duc se não existir
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_pedido'
          AND column_name = 'data_reclamacao'
    ) THEN
ALTER TABLE public.eqv_t_pedido
    ADD COLUMN data_reclamacao date;
END IF;
END
$$;