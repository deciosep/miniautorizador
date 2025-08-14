package br.com.decio.miniautorizador.domain.enums;

// Enum para motivos de rejeicao de transacao
public enum MotivoRejeicao {
    SALDO_INSUFICIENTE("Saldo insuficiente para realizar a transação"),
    SENHA_INVALIDA("Senha do cartão está incorreta"),
    CARTAO_INEXISTENTE("Cartão não encontrado no sistema");

    private final String descricao;
    MotivoRejeicao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
    public boolean IsMotivoSeguranca() {
        return this == SENHA_INVALIDA || this == CARTAO_INEXISTENTE;
    }
    public boolean IsMotivoSaldo() {
        return this == SALDO_INSUFICIENTE;
    }

}
