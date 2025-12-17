DO $$
BEGIN
    -- Adiciona nu_duc se não existir
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_pagamento'
          AND column_name = 'nu_duc'
    ) THEN
ALTER TABLE public.eqv_t_pagamento
    ADD COLUMN nu_duc numeric;
END IF;

    -- Adiciona referencia se não existir
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_pagamento'
          AND column_name = 'referencia'
    ) THEN
ALTER TABLE public.eqv_t_pagamento
    ADD COLUMN referencia numeric;
END IF;

    -- Adiciona entidade se não existir
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_pagamento'
          AND column_name = 'entidade'
    ) THEN
ALTER TABLE public.eqv_t_pagamento
    ADD COLUMN entidade varchar;
END IF;

    -- Adiciona data_prev_pag se não existir
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_pagamento'
          AND column_name = 'data_prev_pag'
    ) THEN
ALTER TABLE public.eqv_t_pagamento
    ADD COLUMN data_prev_pag date;
END IF;


    -- Adiciona user_delete_id se não existir
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_pagamento'
          AND column_name = 'duc_agregado'
    ) THEN
ALTER TABLE public.eqv_t_pagamento
    ADD COLUMN duc_agregado numeric;
END IF;

    -- Adiciona dt_update se não existir
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_pagamento'
          AND column_name = 'dt_update'
    ) THEN
ALTER TABLE public.eqv_t_pagamento
    ADD COLUMN dt_update date;
END IF;

    -- Adiciona user_pag_id se não existir
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_pagamento'
          AND column_name = 'user_pag_id'
    ) THEN
ALTER TABLE public.eqv_t_pagamento
    ADD COLUMN user_pag_id numeric;
END IF;


    -- Adiciona dm_tipo_pagamento se não existir
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_pagamento'
          AND column_name = 'dm_tipo_pagamento'
    ) THEN
ALTER TABLE public.eqv_t_pagamento
    ADD COLUMN dm_tipo_pagamento varchar;
END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_pagamento'
          AND column_name = 'data_checked'
    ) THEN
ALTER TABLE public.eqv_t_pagamento
    ADD COLUMN data_checked date;
END IF;

END
$$;
