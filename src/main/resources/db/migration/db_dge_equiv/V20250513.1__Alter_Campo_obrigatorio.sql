ALTER TABLE eqv_t_tipo_documento
ALTER COLUMN obrigatorio TYPE INTEGER
USING obrigatorio::INT;
