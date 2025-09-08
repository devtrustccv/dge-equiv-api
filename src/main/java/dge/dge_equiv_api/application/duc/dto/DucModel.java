package dge.dge_equiv_api.application.duc.dto;
 public class DucModel {

        private String duc;
        private String codigo;
        private String Entidade;
        private String referencia;
        private String montante;
        private String descricao;
        private String linkDuc;

        public String getDuc() {
            return duc;
        }

        public void setDuc(String duc) {
            this.duc = duc;
        }

        public String getLinkDuc() {
            return linkDuc;
        }

        public void setLinkDuc(String linkDuc) {
            this.linkDuc = linkDuc;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public String getMontante() {
            return montante;
        }

        public void setMontante(String montante) {
            this.montante = montante;
        }

        public String getReferencia() {
            return referencia;
        }

        public void setReferencia(String referencia) {
            this.referencia = referencia;
        }

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getEntidade() {
            return Entidade;
        }

        public void setEntidade(String entidade) {
            Entidade = entidade;
        }
}
