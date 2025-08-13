package br.com.decio.miniautorizador.domain.dto;

import br.com.decio.miniautorizador.domain.enums.MotivoRejeicao;

import java.time.LocalDateTime;

// Request para transacao
public record ResultadoAutorizacao(
        boolean autorizada, MotivoRejeicao motivo, LocalDateTime timeStamp
) {

        public static ResultadoAutorizacao aprovada(){
                return new ResultadoAutorizacao(true, null, LocalDateTime.now());
        }

        public static ResultadoAutorizacao rejeitada(MotivoRejeicao motivo){
                return new ResultadoAutorizacao(false, motivo, LocalDateTime.now());
        }

        public boolean isRejeitada() {
                return !autorizada;
        }

        public boolean isRejeitaPorSeguro() {
                return isRejeitada() && motivo != null && motivo().IsMotivoSeguranca();
        }

        public boolean isRejeitaPorSaldo() {
                return isRejeitada() && motivo != null && motivo().IsMotivoSaldo();
        }

        public String getMensagemStatus() {
                return autorizada? "OK" : motivo.name();
        }
}

