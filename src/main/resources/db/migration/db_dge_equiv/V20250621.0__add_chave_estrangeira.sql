-- 1. Adicionar a coluna 'id_requisicao' se não existir
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqv_t_documento'
          AND column_name = 'id_requisicao'
    ) THEN
ALTER TABLE public.eqv_t_documento
    ADD COLUMN id_requisicao INTEGER;
END IF;
END
$$;

-- 2. Remover a constraint errada, se existir
ALTER TABLE public.eqv_t_documento
DROP CONSTRAINT IF EXISTS eqv_t__id_tipo_equesicao_fkey;

-- 3. Adicionar a foreign key correta se não existir
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'eqv_t_documento_id_requisicao_fkey'
          AND table_name = 'eqv_t_documento'
    ) THEN
ALTER TABLE public.eqv_t_documento
    ADD CONSTRAINT eqv_t_documento_id_requisicao_fkey
        FOREIGN KEY (id_requisicao)
            REFERENCES public.eqv_t_requisicao(id);
END IF;
END
$$;
