CREATE TABLE IF NOT EXISTS  eqv_t_requerente  (
                                  id SERIAL PRIMARY KEY,
                                  nome CHARACTER VARYING(200),
                                  nif INTEGER,
                                  doc_numero CHARACTER VARYING,
                                  data_nascimento DATE,
                                  data_emissao_doc DATE,
                                  data_validade_doc DATE,
                                  nacionalidade CHARACTER VARYING,
                                  sexo CHARACTER VARYING,
                                  email CHARACTER VARYING,
                                  contato INTEGER,
                                  habilitacao INTEGER,
                                  doc_identificacao CHARACTER VARYING,
                                  user_create INTEGER,
                                  user_update INTEGER,
                                  date_create DATE,
                                  data_update DATE
);

CREATE TABLE IF NOT EXISTS eqv_t_inst_ensino (
                                   id SERIAL PRIMARY KEY,
                                   nome CHARACTER VARYING,
                                   endereco CHARACTER VARYING(255),
                                   email CHARACTER VARYING,
                                   contato INTEGER,
                                   pais CHARACTER VARYING,
                                   website CHARACTER VARYING,
                                   status CHARACTER VARYING,
                                   date_create DATE,
                                   date_update DATE,
                                   user_create INTEGER,
                                   user_update INTEGER
);

CREATE TABLE IF NOT EXISTS eqv_t_requisicao (
                                  id SERIAL PRIMARY KEY,
                                  status INTEGER,
                                  etapa INTEGER,
                                  n_processo CHARACTER VARYING,
                                  user_create INTEGER,
                                  user_update INTEGER,
                                  data_create DATE,
                                  data_update DATE
);

CREATE TABLE IF NOT EXISTS eqv_t_pedido (
                              id SERIAL PRIMARY KEY,
                              id_requerente INTEGER REFERENCES eqv_t_requerente(id),
                              id_inst_ensino INTEGER REFERENCES eqv_t_inst_ensino(id),
                              id_requisicao INTEGER REFERENCES eqv_t_requisicao(id),
                              formacao_prof CHARACTER VARYING(255),
                              carga INTEGER,
                              ano_inicio NUMERIC,
                              ano_fim NUMERIC,
                              nivel INTEGER,
                              familia CHARACTER VARYING,
                              despacho CHARACTER VARYING,
                              status INTEGER,
                              num_declaracao CHARACTER VARYING(255),
                              etapa CHARACTER VARYING
);

CREATE TABLE IF NOT EXISTS eqvt_t_decisao_vp (
                                   id SERIAL PRIMARY KEY,
                                   id_pedido INTEGER REFERENCES eqv_t_pedido(id),
                                   nivel INTEGER,
                                   familia CHARACTER VARYING(255),
                                   decisao INTEGER,
                                   data_create DATE,
                                   user_create INTEGER,
                                   data_update DATE,
                                   user_update INTEGER,
                                   obs_vp CHARACTER VARYING
);

CREATE TABLE IF NOT EXISTS eqvt_t_decisao_ap (
                                   id SERIAL PRIMARY KEY,
                                   id_pedido INTEGER REFERENCES eqv_t_pedido(id),
                                   decisao INTEGER,
                                   obs CHARACTER VARYING,
                                   user_create INTEGER,
                                   user_update INTEGER,
                                   data_create DATE,
                                   data_update DATE,
                                   familia CHARACTER VARYING,
                                   parecer_cnep CHARACTER VARYING,
                                   ata CHARACTER VARYING
);

CREATE TABLE IF NOT EXISTS eqvt_t_decisao_despacho (
                                         id SERIAL PRIMARY KEY,
                                         id_pedido INTEGER REFERENCES eqv_t_pedido(id),
                                         decisao INTEGER,
                                         obs_despacho CHARACTER VARYING,
                                         user_create INTEGER,
                                         user_update INTEGER,
                                         data_create DATE,
                                         data_update DATE
);

CREATE TABLE IF NOT EXISTS eqv_t_reclamacao (
                                  id SERIAL PRIMARY KEY,
                                  id_pedido INTEGER REFERENCES eqv_t_pedido(id),
                                  id_requisicao INTEGER REFERENCES eqv_t_requisicao(id),
                                  observacao CHARACTER VARYING,
                                  decisao INTEGER,
                                  anexo CHARACTER VARYING,
                                  user_create INTEGER,
                                  user_update INTEGER,
                                  date_create DATE,
                                  date_update DATE
);

CREATE TABLE IF NOT EXISTS eqv_t_tipo_documento (
                                       id SERIAL PRIMARY KEY,
                                       nome CHARACTER VARYING,
                                       descricao CHARACTER VARYING,
                                       status INTEGER,
                                       obrigatorio BOOLEAN,
                                       processo INTEGER,
                                       etapa CHARACTER VARYING,
                                       user_create INTEGER,
                                       user_update INTEGER,
                                       date_create DATE,
                                       date_update DATE
);

CREATE TABLE IF NOT EXISTS eqv_t_documento (
                                 id SERIAL PRIMARY KEY,
                                 id_pedido INTEGER REFERENCES eqv_t_pedido(id),
                                 id_etapa INTEGER REFERENCES eqv_t_requisicao(id),
                                 n_processo INTEGER,
                                 id_tipo_documento INTEGER REFERENCES eqv_t_tipo_documento(id),
                                 uuid_anexo CHARACTER VARYING(255),
                                 user_create INTEGER,
                                 user_update INTEGER,
                                 date_create DATE,
                                 date_update DATE
);

CREATE TABLE IF NOT EXISTS eqv_t_pagamento (
                                 id SERIAL PRIMARY KEY,
                                 id_pedido INTEGER REFERENCES eqv_t_pedido(id),
                                 id_requisicao INTEGER REFERENCES eqv_t_requisicao(id),
                                 total DECIMAL,
                                 estado INTEGER,
                                 nr_processo INTEGER,
                                 nu_cheque CHARACTER VARYING(255),
                                 banco_id INTEGER,
                                 id_task INTEGER,
                                 tipo_pagamento CHARACTER VARYING(255),
                                 link_duc CHARACTER VARYING(255),
                                 data_pagamento DATE,
                                 user_registro INTEGER,
                                 data_registro DATE
);
