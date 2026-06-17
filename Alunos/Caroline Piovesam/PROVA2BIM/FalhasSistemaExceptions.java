package com.caroline.prova2bim.erros;

public class FalhasSistemaExceptions extends Exception { // herda do lava.lang.Exception

    public FalhasSistemaExceptions(String msg, Throwable causa) {
        super(msg, causa);
    }

    public FalhasSistemaExceptions(String msg) { // caso não tenha uma causa
        super(msg);
    }
}