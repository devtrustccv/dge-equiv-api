CREATE TABLE IF NOT EXISTS EQV_T_TAXA (
    ID        SERIAL PRIMARY KEY,          -- Identificador único (auto-incremento)
                            ESTADO    VARCHAR(100) ,       -- Ex: "Em curso", "Concluído"
                            ETAPA     VARCHAR(100) ,       -- Ex: "Pedido", "Aprovação", etc.
                            VALOR     DECIMAL ,
    user_create integer,
    user_update integer,
    data_create date,
    data_update date


);
