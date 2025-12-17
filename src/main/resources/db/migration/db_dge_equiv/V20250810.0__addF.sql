-- Adicionar a coluna id_taxa caso não exista
ALTER TABLE public.eqv_t_pagamento
    ADD COLUMN IF NOT EXISTS id_taxa integer;

-- Criar a constraint apenas se ainda não existir
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'eqv_t_pagamento_id_taxa_fkey'
          AND table_name = 'eqv_t_pagamento'
    ) THEN
ALTER TABLE public.eqv_t_pagamento
    ADD CONSTRAINT eqv_t_pagamento_id_taxa_fkey
        FOREIGN KEY (id_taxa)
            REFERENCES public.eqv_t_taxa (id)
            ON UPDATE NO ACTION
            ON DELETE NO ACTION;
END IF;
END$$;
