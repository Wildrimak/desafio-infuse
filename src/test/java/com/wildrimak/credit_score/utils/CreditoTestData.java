package com.wildrimak.credit_score.utils;

import com.wildrimak.credit_score.domain.models.Credito;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreditoTestData {

    public static Credito credito(String numero, String nfse, int diasAtras, BigDecimal valor) {
        Credito credito = new Credito();
        credito.setNumeroCredito(numero);
        credito.setNumeroNfse(nfse);
        credito.setDataConstituicao(LocalDate.now().minusDays(diasAtras));
        credito.setValorIssqn(valor);
        return credito;
    }

    public static Credito creditoC001() {
        return credito("C001", "NFSE001", 10, new BigDecimal("100.50"));
    }

    public static Credito creditoC002() {
        return credito("C002", "NFSE001", 5, new BigDecimal("50.25"));
    }

    public static Credito creditoC003() {
        return credito("C003", "NFSE002", 20, new BigDecimal("200.00"));
    }

}

