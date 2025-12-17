CREATE TABLE if not exists public.eqv_t_param_report (
                                           id SERIAL PRIMARY KEY,
                                           caixa_postal VARCHAR(255),
                                           email VARCHAR(255),
                                           logotipo BYTEA,
                                           republica VARCHAR(255),
                                           rua VARCHAR(255),
                                           telefone VARCHAR(255)
);