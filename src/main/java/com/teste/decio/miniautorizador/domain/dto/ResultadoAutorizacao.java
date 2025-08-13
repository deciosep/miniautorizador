package com.teste.decio.miniautorizador.domain.dto;

import com.teste.decio.miniautorizador.domain.enums.MotivoRejeicao;

// Request para transacao
public record ResultadoAutorizacao(
        boolean autorizada, MotivoRejeicao motivo
) {

        public static ResultadoAutorizacao autorizada(){
                return new ResultadoAutorizacao(true, null);
        }

        public static ResultadoAutorizacao rejeitada(MotivoRejeicao motivo){
                return new ResultadoAutorizacao(false, motivo);
        }

}

