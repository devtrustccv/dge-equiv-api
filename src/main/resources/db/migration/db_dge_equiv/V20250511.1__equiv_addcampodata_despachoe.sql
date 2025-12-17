DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_pedido'
          AND column_name = 'data_despacho'
          AND table_schema = 'public'
    ) THEN
ALTER TABLE public.eqv_t_pedido
    ADD COLUMN data_despacho date;
END IF;
END
$$;
