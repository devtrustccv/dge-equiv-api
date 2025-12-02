DO $$
BEGIN
    -- Adiciona nu_duc se não existir
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_pagamento'
          AND column_name = 'etapa'
    ) THEN
ALTER TABLE public.eqv_t_pagamento
    ADD COLUMN etapa character varying(255);
END IF;

END
$$;