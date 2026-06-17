package com.example.exception;

public class SeriesException extends Exception{

    public enum TipoErro {
        USUARIO_NAO_ENCONTADO,
        ERRO_CONEXAO_API,
        SERIE_NAO_ENCONTRADA,
        FALHA_GRAVACAO,
        TEMPO_EXCEDIDO
    }

    private final TipoErro tipo;

    public SeriesException(String msg, TipoErro tipo) {
        super(msg);
        this.tipo = tipo;
    }

    public TipoErro getTipo() {
        return tipo;
    }
}
