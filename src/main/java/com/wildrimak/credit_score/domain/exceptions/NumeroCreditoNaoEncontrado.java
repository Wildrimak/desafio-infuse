package com.wildrimak.credit_score.domain.exceptions;

public class NumeroCreditoNaoEncontrado extends RuntimeException {

    private final String numeroCredito;

    public NumeroCreditoNaoEncontrado(String numeroCredito) {
        super("Número de crédito '" + numeroCredito + "' não encontrado.");
        this.numeroCredito = numeroCredito;
    }

    public String getNumeroCredito() {
        return numeroCredito;
    }

}
