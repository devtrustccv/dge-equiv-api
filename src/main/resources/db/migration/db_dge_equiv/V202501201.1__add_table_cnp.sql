-- Table: public.eqv_t_cnep

-- DROP TABLE IF EXISTS public.eqv_t_cnep;

CREATE TABLE IF NOT EXISTS public.eqv_t_cnep
(
    id serial PRIMARY KEY NOT NULL,
    nome character varying COLLATE pg_catalog."default" NOT NULL,
    instituicao character varying COLLATE pg_catalog."default" NOT NULL,
    email character varying COLLATE pg_catalog."default" NOT NULL,
    user_create integer NOT NULL,
    date_create date NOT NULL,
    user_update integer,
    date_update date,
    "status" character varying
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.eqv_t_cnep
    OWNER to postgres;

-- add campo tabebe a eqv_decicao_ vp

DO $$
BEGIN
    -- Adiciona id_pessoa se não existir
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'eqvt_t_decisao_vp'
          AND column_name = 'motivo_retificado'
    ) THEN
ALTER TABLE public.eqvt_t_decisao_vp
    ADD COLUMN motivo_retificado character varying;
END IF;
END;
$$;