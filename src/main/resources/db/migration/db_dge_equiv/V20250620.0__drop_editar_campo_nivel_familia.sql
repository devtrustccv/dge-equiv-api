DROP VIEW IF EXISTS public.vw_requisicao;


ALTER TABLE public.eqv_t_pedido

ALTER COLUMN familia TYPE varchar(255);

CREATE OR REPLACE VIEW public.vw_requisicao AS
SELECT
    r.id AS id_requisicao,
    r.status,
    r.etapa,
    r.n_processo,
    r.data_create,
    r.user_update,
    r.data_update,
    r.user_create,
    p.id AS id_pedido,
    req.nome AS nome_requerente,
    inst.nome AS nomeinstituicaoensino
FROM eqv_t_requisicao r
         LEFT JOIN LATERAL (
    SELECT
        p_1.id,
        p_1.id_requerente,
        p_1.id_inst_ensino,
        p_1.id_requisicao,
        p_1.formacao_prof,
        p_1.carga,
        p_1.ano_inicio,
        p_1.ano_fim,
        p_1.nivel,
        p_1.familia,  -- agora varchar
        p_1.despacho,
        p_1.status,
        p_1.num_declaracao,
        p_1.etapa,
        p_1.data_despacho
    FROM eqv_t_pedido p_1
    WHERE p_1.id_requisicao = r.id
    ORDER BY p_1.id
        LIMIT 1
 ) p ON true
    LEFT JOIN eqv_t_requerente req ON p.id_requerente = req.id
    LEFT JOIN eqv_t_inst_ensino inst ON p.id_inst_ensino = inst.id;

ALTER TABLE public.eqvt_t_decisao_vp

ALTER COLUMN familia TYPE varchar(255);

ALTER TABLE public.eqvt_t_decisao_ap

ALTER COLUMN familia TYPE varchar(255);
