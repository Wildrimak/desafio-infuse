package com.wildrimak.credit_score.domain.exceptions;

public class NumeroCreditoNaoEncontradoException extends RuntimeException {

    private final String numeroCredito;

    public NumeroCreditoNaoEncontradoException(String numeroCredito) {
        super("Número de crédito '" + numeroCredito + "' não encontrado.");
        this.numeroCredito = numeroCredito;
    }

    public String getNumeroCredito() {
        return numeroCredito;
    }

}
